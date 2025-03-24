package uz.futuresoft.applockdemo.presentation.activities.main

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.data.ContextCache
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import uz.futuresoft.applockdemo.presentation.navigation.Screens
import uz.futuresoft.applockdemo.presentation.screens.apps.navigation.appsRoute
import uz.futuresoft.applockdemo.presentation.screens.permissions.navigation.permissionsRoute
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val isNotificationPermissionAllowed by remember {
                mutableStateOf(isNotificationPermissionAllowed(context = this))
            }
            val isUsageAccessPermissionAllowed by remember {
                mutableStateOf(isUsageAccessPermissionAllowed(context = this))
            }
            val isOverlayPermissionAllowed by remember {
                mutableStateOf(isOverlayPermissionAllowed(context = this))
            }

            LaunchedEffect(
                key1 = isUsageAccessPermissionAllowed,
                key2 = isOverlayPermissionAllowed,
                key3 = isNotificationPermissionAllowed,
            ) {
                if (
                    isUsageAccessPermissionAllowed &&
                    isOverlayPermissionAllowed &&
                    isNotificationPermissionAllowed
                ) {
                    viewModel.onAction(MainAction.PermissionsAllowed)
                }
            }

            AppLockDemoTheme {
                MainActivityContent(
                    startDestination = uiState.startDestination,
                )
            }
        }
    }
}

@Composable
fun MainActivityContent(
    startDestination: Screens,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        permissionsRoute(navController = navController)
        appsRoute(navController = navController)
    }
}

@Preview
@Composable
fun MainActivityContentPreview() {
    AppLockDemoTheme {
        MainActivityContent(
            startDestination = Screens.Apps
        )
    }
}

private fun isNotificationPermissionAllowed(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

private fun isUsageAccessPermissionAllowed(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    } else {
        appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

private fun isOverlayPermissionAllowed(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}
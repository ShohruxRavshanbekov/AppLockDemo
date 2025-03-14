package uz.futuresoft.applockdemo.presentation

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.futuresoft.applockdemo.presentation.components.AppItem
import uz.futuresoft.applockdemo.presentation.components.PrimaryAlertDialog
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.presentation.utils.AppInfo
import uz.futuresoft.applockdemo.presentation.view_model.SharedAction
import uz.futuresoft.applockdemo.presentation.view_model.SharedState
import uz.futuresoft.applockdemo.presentation.view_model.SharedViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRequiredPermissions(activity = this)

        enableEdgeToEdge()
        setContent {
            val sharedUiState by viewModel.sharedUiState.collectAsStateWithLifecycle()
            val usageAccessAllowed by remember {
                mutableStateOf(isUsageAccessPermissionAllowed(context = this))
            }
            val overlayPermissionAllowed by remember {
                mutableStateOf(isOverlayPermissionAllowed(context = this))
            }


            if (usageAccessAllowed) {
                startAppBlockerService(context = this)
            }

            LaunchedEffect(key1 = Unit) {
                viewModel.onAction(SharedAction.GetApps(context = this@MainActivity))
            }

            AppLockDemoTheme {
                Content(
                    context = this,
                    sharedUiState = sharedUiState,
                    usageAccessAllowed = usageAccessAllowed,
                    overlayPermissionAllowed = overlayPermissionAllowed,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}

@Composable
fun Content(
    context: Context,
    sharedUiState: SharedState,
    usageAccessAllowed: Boolean,
    overlayPermissionAllowed: Boolean,
    onAction: (SharedAction) -> Unit,
) {
    var showUsageAccessAlertDialog by remember { mutableStateOf(!usageAccessAllowed) }
    var showOverlayAlertDialog by remember { mutableStateOf(!overlayPermissionAllowed) }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            items(sharedUiState.apps) { appInfo ->
                AppItem(
                    appInfo = appInfo,
                    onChangeBlockedStatus = {
                        onAction(
                            SharedAction.OnChangeAppBlockedStatus(
                                status = it,
                                packageName = appInfo.packageName
                            )
                        )
                    },
                )
            }
        }

        if (showUsageAccessAlertDialog) {
            PrimaryAlertDialog(
                onDismissRequest = { showUsageAccessAlertDialog = false },
                onConfirm = {
                    requestUsageAccessPermission(context = context)
                    showUsageAccessAlertDialog = false
                },
                title = "\"Usage Access\" is not enabled!",
                text = "For the app to work correctly, please enable the permission \"Usage Access\".",
            )
        }

        if (usageAccessAllowed && showOverlayAlertDialog) {
            PrimaryAlertDialog(
                onDismissRequest = { showOverlayAlertDialog = false },
                onConfirm = {
                    requestOverlayPermission(context = context)
                    showOverlayAlertDialog = false
                },
                title = "\"Appear on top of other apps\" is not enabled!",
                text = "For the app to work correctly, please enable the permission \"Appear on top of other apps\".",
            )
        }
    }
}

@Preview
@Composable
fun MainActivityContentPreview() {
    AppLockDemoTheme {
        Content(
            context = LocalContext.current,
            sharedUiState = SharedState(
                apps = listOf(
                    AppInfo(
                        name = "Instagram",
                        packageName = "com.android.instagram",
                        icon = null,
                        locked = false,
                    ),
                    AppInfo(
                        name = "YouTube",
                        packageName = "com.android.youtube",
                        icon = null,
                        locked = false,
                    ),
                    AppInfo(
                        name = "Chrome",
                        packageName = "com.google.android.chrome",
                        icon = null,
                        locked = false,
                    ),
                ),
            ),
            usageAccessAllowed = true,
            overlayPermissionAllowed = true,
            onAction = {},
        )
    }
}

private fun requestRequiredPermissions(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0,
        )
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

private fun startAppBlockerService(context: Context) {
    val intent = Intent(context, AppBlockerService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

private fun requestUsageAccessPermission(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

private fun isOverlayPermissionAllowed(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

private fun requestOverlayPermission(context: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        "package:${context.packageName}".toUri()
    )
    context.startActivity(intent)
}
package uz.futuresoft.applockdemo.presentation.screens.permissions

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uz.futuresoft.applockdemo.R
import uz.futuresoft.applockdemo.presentation.AppBlockerService
import uz.futuresoft.applockdemo.presentation.activities.main.MainActivity
import uz.futuresoft.applockdemo.presentation.navigation.Screens
import uz.futuresoft.applockdemo.presentation.screens.permissions.components.PermissionPage
import uz.futuresoft.applockdemo.presentation.screens.permissions.utils.PermissionModel
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme

@Composable
fun PermissionsScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isNotificationPermissionAllowed by remember { mutableStateOf(false) }
    var isUsageAccessPermissionAllowed by remember { mutableStateOf(false) }
    var isOverlayPermissionAllowed by remember { mutableStateOf(false) }

    val requestNotificationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isAllowed ->
            isNotificationPermissionAllowed = isAllowed
        }
    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isUsageAccessPermissionAllowed = isUsageAccessPermissionAllowed(context = context)
                isOverlayPermissionAllowed = isOverlayPermissionAllowed(context = context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    PermissionsScreenContent(
        isNotificationPermissionAllowed = isNotificationPermissionAllowed,
        isUsageAccessPermissionAllowed = isUsageAccessPermissionAllowed,
        isOverlayPermissionAllowed = isOverlayPermissionAllowed,
        onAction = { action ->
            when (action) {
                PermissionsAction.RequestUsageAccessPermission -> requestUsageAccessPermission(
                    context
                )

                PermissionsAction.RequestOverlayPermission -> requestOverlayPermission(context)
                PermissionsAction.NavigateToAppsScreen -> navController.navigate(Screens.Apps) {
                    popUpTo(Screens.Permissions) { inclusive = true }
                }

                PermissionsAction.RequestNotificationPermission -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    )
}

@Composable
fun PermissionsScreenContent(
    isNotificationPermissionAllowed: Boolean,
    isUsageAccessPermissionAllowed: Boolean,
    isOverlayPermissionAllowed: Boolean,
    onAction: (PermissionsAction) -> Unit,
) {
    val context = LocalContext.current
    val permissions = remember {
        mutableStateListOf(
            PermissionModel(
                icon = R.drawable.notification,
                name = context.getString(R.string.permission_notification),
                description = context.getString(R.string.permission_description_notification),
            ),
            PermissionModel(
                icon = R.drawable.usage_access,
                name = context.getString(R.string.permission_usage_access),
                description = context.getString(R.string.permission_description_usage_access),
            ),
            PermissionModel(
                icon = R.drawable.overlay,
                name = context.getString(R.string.permission_overlay),
                description = context.getString(R.string.permission_description_overlay),
            ),
        )
    }
    val pagerState = rememberPagerState { permissions.size }

    LaunchedEffect(
        key1 = isUsageAccessPermissionAllowed,
        key2 = isOverlayPermissionAllowed,
        key3 = isNotificationPermissionAllowed
    ) {
        when {
            isNotificationPermissionAllowed -> pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
            isOverlayPermissionAllowed -> {
                onAction(PermissionsAction.NavigateToAppsScreen)
                Log.d("AAAAA", "PermissionsScreenContent: it works")
            }

            isUsageAccessPermissionAllowed -> {
                startAppBlockerService(context = context)
                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
            }
        }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    when (pagerState.currentPage) {
                        0 -> onAction(PermissionsAction.RequestNotificationPermission)
                        1 -> onAction(PermissionsAction.RequestUsageAccessPermission)
                        2 -> onAction(PermissionsAction.RequestOverlayPermission)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 30.dp),
            ) {
                Text(text = stringResource(R.string.allow))
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr) + 30.dp,
                        end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr) + 30.dp,
                    )
                ),
            userScrollEnabled = false,
        ) { page ->
            PermissionPage(permission = permissions[page])
        }
    }
}

@Preview
@Composable
private fun PermissionsScreenPreview() {
    AppLockDemoTheme {
        PermissionsScreenContent(
            isNotificationPermissionAllowed = true,
            isUsageAccessPermissionAllowed = true,
            isOverlayPermissionAllowed = true,
            onAction = {},
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

private fun isOverlayPermissionAllowed(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

private fun requestUsageAccessPermission(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

private fun requestOverlayPermission(context: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        "package:${context.packageName}".toUri()
    )
    context.startActivity(intent)
}
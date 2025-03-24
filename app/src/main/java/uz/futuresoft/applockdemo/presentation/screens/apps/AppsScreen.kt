@file:OptIn(ExperimentalMaterial3Api::class)

package uz.futuresoft.applockdemo.presentation.screens.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import uz.futuresoft.applockdemo.presentation.screens.apps.components.AppItem
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.presentation.screens.apps.utils.AppInfo

@Composable
fun AppsScreen(
    navController: NavHostController,
    viewModel: AppsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppsScreenContent(
        uiState = uiState,
        usageAccessAllowed = true,
        overlayPermissionAllowed = true,
        onAction = viewModel::onAction,
    )
}

@Composable
fun AppsScreenContent(
    uiState: AppsState,
    usageAccessAllowed: Boolean,
    overlayPermissionAllowed: Boolean,
    onAction: (AppsAction) -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    var showUsageAccessAlertDialog by remember { mutableStateOf(!usageAccessAllowed) }
    var showOverlayAlertDialog by remember { mutableStateOf(!overlayPermissionAllowed) }

    LaunchedEffect(key1 = uiState.loading) {
        isRefreshing = uiState.loading
    }

    Scaffold { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.loading,
            onRefresh = { onAction(AppsAction.RefreshData) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pullToRefreshState,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                items(uiState.apps) { appInfo ->
                    AppItem(
                        appInfo = appInfo,
                        onChangeLockStatus = {
                            onAction(
                                AppsAction.ChangeAppLockedStatus(
                                    status = it,
                                    packageName = appInfo.packageName
                                )
                            )
                        },
                    )
                }
            }
        }

//        if (showUsageAccessAlertDialog) {
//            PrimaryAlertDialog(
//                onDismissRequest = { showUsageAccessAlertDialog = false },
//                onConfirm = {
//                    requestUsageAccessPermission(context = context)
//                    showUsageAccessAlertDialog = false
//                },
//                title = "\"Usage Access\" is not enabled!",
//                text = "For the app to work correctly, please enable the permission \"Usage Access\".",
//            )
//        }
//
//        if (usageAccessAllowed && showOverlayAlertDialog) {
//            PrimaryAlertDialog(
//                onDismissRequest = { showOverlayAlertDialog = false },
//                onConfirm = {
//                    requestOverlayPermission(context = context)
//                    showOverlayAlertDialog = false
//                },
//                title = "\"Appear on top of other apps\" is not enabled!",
//                text = "For the app to work correctly, please enable the permission \"Appear on top of other apps\".",
//            )
//        }
    }
}

@Preview
@Composable
private fun AppsScreenPreview() {
    AppLockDemoTheme {
        AppsScreenContent(
            uiState = AppsState(
                apps = listOf(
                    AppInfo(
                        name = "Instagram",
                        packageName = "com.android.instagram",
                        locked = false,
                    ),
                    AppInfo(
                        name = "YouTube",
                        packageName = "com.android.youtube",
                        locked = false,
                    ),
                    AppInfo(
                        name = "Chrome",
                        packageName = "com.google.android.chrome",
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
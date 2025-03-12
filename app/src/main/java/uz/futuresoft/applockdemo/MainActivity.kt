package uz.futuresoft.applockdemo

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.futuresoft.applockdemo.ui.theme.AppLockDemoTheme
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppLockDemoTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    val context = LocalContext.current
    val apps by remember { mutableStateOf(getLaunchableApps(context = context)) }
    val isAccessibilityServiceEnabled by remember {
        mutableStateOf(
            isAccessibilityServiceEnabled(
                context = context,
                serviceClass = AppBlockerService::class.java,
            )
        )
    }
    var showAlertDialog by remember { mutableStateOf(!isAccessibilityServiceEnabled) }

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            items(apps) { appInfo ->
                AppItem(
                    appInfo = appInfo,
                    onChangeBlockedStatus = {},
                )
            }
        }

        if (showAlertDialog) {
            AccessibilityServiceIsNotEnableDialog(
                onDismissRequest = { showAlertDialog = false },
                onConfirm = {
                    openAccessibilitySettings(context = context)
                    showAlertDialog = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppLockDemoTheme {
        Content()
    }
}

fun getLaunchableApps(context: Context): List<AppInfo> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val apps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

    return apps.map {
        AppInfo(
            name = it.loadLabel(packageManager).toString(),
            packageName = it.activityInfo.packageName,
            icon = it.loadIcon(packageManager)
        )
    }
}

private fun getUserInstalledApps(context: Context): List<ApplicationInfo> {
    val packageManager = context.packageManager
    return packageManager.getInstalledApplications(PackageManager.GET_META_DATA).filter { app ->
        (app.flags and ApplicationInfo.FLAG_SYSTEM == 0)
    }
}

private fun getAppDetails(context: Context, appInfo: ApplicationInfo): Pair<String, Drawable> {
    val packageManager = context.packageManager
    val appName = packageManager.getApplicationLabel(appInfo).toString()
    val appIcon = packageManager.getApplicationIcon(appInfo)
    return Pair(appName, appIcon)
}


private fun isAccessibilityServiceEnabled(
    context: Context,
    serviceClass: Class<out AccessibilityService>
): Boolean {
    val componentName = ComponentName(context, serviceClass)
//    val accessibilityManager =
//        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    val accessibilityEnabled =
        Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
    return accessibilityEnabled == 1 && enabledServices?.contains(componentName.flattenToString()) == true
}

private fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}
package uz.futuresoft.applockdemo.presentation.activities.lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.presentation.activities.main.MainState

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<LockViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val lockedAppPackageName = intent.getStringExtra("packageName") as String

            LaunchedEffect(key1 = lockedAppPackageName) {
                viewModel.onAction(LockAction.GetApp(lockedAppPackageName))
            }

            AppLockDemoTheme {
                LockScreenActivityContent(uiState = uiState)
            }
        }
    }
}

@Composable
private fun LockScreenActivityContent(uiState: LockState) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "${uiState.app.name} заблокировано")
//            Text(text = "Приложение заблокировано")

            BackHandler {
                closeApp(context = context)
            }
        }
    }
}

@Preview
@Composable
private fun LockScreenActivityPreview() {
    AppLockDemoTheme {
        LockScreenActivityContent(uiState = LockState())
    }
}

private fun closeApp(context: Context) {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(homeIntent)
}
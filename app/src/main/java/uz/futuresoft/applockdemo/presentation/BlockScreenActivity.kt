package uz.futuresoft.applockdemo.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.presentation.view_model.SharedState
import uz.futuresoft.applockdemo.presentation.view_model.SharedViewModel

class BlockScreenActivity : ComponentActivity() {

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sharedUiState by viewModel.sharedUiState.collectAsStateWithLifecycle()
            val blockedAppPackageName = intent.getStringExtra("packageName")

//            LaunchedEffect(key1 = Unit) {
//                viewModel.onAction(
//                    SharedAction.GetApp(
//                        context = this@BlockScreenActivity,
//                        packageName = blockedAppPackageName,
//                    )
//                )
//            }

            AppLockDemoTheme {
                BlockScreenActivityContent(sharedUiState = sharedUiState)
            }
        }
    }
}

@Composable
private fun BlockScreenActivityContent(sharedUiState: SharedState) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Text(text = "${sharedUiState.app?.name} заблокировано")
            Text(text = "Приложение заблокировано")

            BackHandler {
                closeApp(context = context)
            }
        }
    }
}

@Preview
@Composable
private fun BlockScreenActivityPreview() {
    AppLockDemoTheme {
        BlockScreenActivityContent(sharedUiState = SharedState())
    }
}

private fun closeApp(context: Context) {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(homeIntent)
}
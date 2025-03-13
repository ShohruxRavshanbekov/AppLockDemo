package uz.futuresoft.applockdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.futuresoft.applockdemo.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.view_model.SharedAction
import uz.futuresoft.applockdemo.view_model.SharedState
import uz.futuresoft.applockdemo.view_model.SharedViewModel

class BlockScreenActivity : ComponentActivity() {

    private val viewModel: SharedViewModel by viewModels { SharedViewModel.Factory }

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
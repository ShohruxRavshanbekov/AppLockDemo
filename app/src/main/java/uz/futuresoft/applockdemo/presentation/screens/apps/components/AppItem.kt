package uz.futuresoft.applockdemo.presentation.screens.apps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme
import uz.futuresoft.applockdemo.presentation.screens.apps.utils.AppInfo
import uz.futuresoft.applockdemo.utils.getAppIcon

@Composable
fun AppItem(
    appInfo: AppInfo,
    onChangeLockStatus: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var locked by remember { mutableStateOf(appInfo.locked) }
    val appIconBitmap = remember {
        appInfo.packageName.getAppIcon(context).asImageBitmap()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            bitmap = appIconBitmap,
            contentDescription = appInfo.name,
            modifier = Modifier.size(50.dp),
        )
        /*if (appIconBitmap != null) {
            Image(
                bitmap = appIconBitmap,
                contentDescription = appInfo.name,
                modifier = Modifier.size(50.dp),
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = Color.Gray),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Apps,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }*/
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = appInfo.name,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = appInfo.packageName,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
        }
        IconButton(
            onClick = {
                locked = !locked
                onChangeLockStatus(locked)
            }
        ) {
            Icon(
                imageVector = if (locked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun AppItemPreview() {
    AppLockDemoTheme {
        AppItem(
            appInfo = AppInfo(
                name = "App name",
                packageName = "package.name",
                locked = false,
            ),
            onChangeLockStatus = {},
        )
    }
}
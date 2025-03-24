package uz.futuresoft.applockdemo.presentation.screens.permissions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.futuresoft.applockdemo.R
import uz.futuresoft.applockdemo.presentation.screens.permissions.utils.PermissionModel
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme

@Composable
fun PermissionPage(
    permission: PermissionModel,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = permission.icon),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 22.dp)
                .size(150.dp),
        )
        Text(
            text = permission.name,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
        )
        Text(
            text = permission.description,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(color = Color.Black, fontSize = 16.sp, textAlign = TextAlign.Center),
        )
    }
}

@Preview
@Composable
private fun PermissionPagePreview() {
    AppLockDemoTheme {
        val context = LocalContext.current

        PermissionPage(
            permission = PermissionModel(
                icon = R.drawable.usage_access,
                name = context.getString(R.string.permission_usage_access),
                description = context.getString(R.string.permission_description_usage_access),
            )
        )
    }
}
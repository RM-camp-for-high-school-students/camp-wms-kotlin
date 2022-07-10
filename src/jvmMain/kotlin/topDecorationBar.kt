import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun topDecorationBar() {
    MaterialTheme {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(38, 83, 179)).padding(start = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "疆来计划 仓库管理系统", color = Color.White, fontSize = 36.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(410.dp))
                Icon(
                    painterResource("robomaster_logo_white.svg"),
                    contentDescription = "Robomaster Logo",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp, 64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier.height(32.dp).width(1.dp), color = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painterResource("DJI_logo_white.svg"),
                    contentDescription = "DJI Logo",
                    tint = Color.White,
                    modifier = Modifier.size(72.dp, 72.dp)
                )
            }
        }
    }
}
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
                Text(text = "camp-wms", color = Color.White, fontSize = 36.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(700.dp))
                Icon(
                    painterResource("innox_logo.svg"),
                    contentDescription = "Innox Logo",
                    tint = Color.White,
                    modifier = Modifier.size(72.dp, 72.dp)
                )
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}
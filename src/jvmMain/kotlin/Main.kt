import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

var databaseUrl = "jdbc:mysql://rm-wz95tw2q29jk1973ilo.mysql.rds.aliyuncs.com:3306"
var databaseUserName = ""
var databasePassword = ""

var currentDatabase = "test"

var isDatabaseConnected = false

val lightBlue = Color(0xffd8e6ff)
val blue = Color(0xff76a9ff)

fun main() = application {
    val windowState = rememberWindowState()
    windowState.size = DpSize(1024.dp, 768.dp)
    Window(
        onCloseRequest = {},
        icon = painterResource("robomaster_logo_black.svg"),
        state = windowState,
        undecorated = true,
        resizable = false,
        title = "疆来计划 仓库管理系统"
    ) {
        //顶栏右侧最小化及关闭按钮
        WindowDraggableArea {
            Row(
                modifier = Modifier.fillMaxWidth().height(32.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.fillMaxHeight().clickable {
                    windowState.isMinimized = true
                }.padding(start = 6.dp, end = 6.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Minimize, contentDescription = "Minimize")
                }
                Box(modifier = Modifier.fillMaxHeight().clickable {
                    exitApplication()
                }.padding(start = 6.dp, end = 6.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
        Column {
            //顶部装饰栏
            topDecorationBar()
            Spacer(modifier = Modifier.height(36.dp))
            mainContents()
        }
    }
}


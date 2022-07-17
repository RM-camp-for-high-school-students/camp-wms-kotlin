import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.DriverManager

@Composable
fun homePageContents() {
    var goodsCount by remember { mutableStateOf(0) }
    var goodsRented by remember { mutableStateOf(0) }
    var goodsAvailable by remember { mutableStateOf(0) }
    var goodsRemoved by remember { mutableStateOf(0) }
    var memberCount by remember { mutableStateOf(0) }
    var groupCount by remember { mutableStateOf(0) }
    var isDatabaseAvailable by remember { mutableStateOf(false) }

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword)
    if (isDatabaseAvailable) {
        Class.forName("com.mysql.cj.jdbc.Driver")
        var conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
        var stmt = conn.createStatement()
        stmt.execute("use $currentDatabase")
        var rs = stmt.executeQuery("select count(*) from rm_goods")
        rs.next()
        goodsCount = rs.getInt("count(*)")
        rs = stmt.executeQuery("select count(isAvailable) from rm_goods where isAvailable = 1")
        rs.next()
        goodsAvailable = rs.getInt("count(isAvailable)")
        goodsRented = goodsCount - goodsAvailable
        rs = stmt.executeQuery("select count(*) from group_members")
        rs.next()
        memberCount = rs.getInt("count(*)")
        rs = stmt.executeQuery("select count(*)from rm_group")
        rs.next()
        groupCount = rs.getInt("count(*)")
        conn.close()

        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Text(
                    text = "主页",
                    fontSize = 32.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "概览",
                    fontSize = 24.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        text = "当前仓库中共有$goodsCount" + "件物资，其中$goodsRented" + "件已借出，$goodsAvailable" + "件可供借用。",
                        fontSize = 18.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "当前数据库中共有$groupCount" + "个小组，$memberCount" + "位营员。",
                        fontSize = 18.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "最近操作",
                    fontSize = 24.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                    stmt = conn.createStatement()
                    stmt.execute("use $currentDatabase")
                    rs = stmt.executeQuery(
                        "select transactionTime, transactionType, memberID, goodsID, goodsName\n" +
                                "from transactions\n" +
                                "         join rm_goods using (goodsID)\n" +
                                "order by transactionTime desc\n" +
                                "limit 3"
                    )
                    while (rs.next()) {
                        commonMessageCard(
                            rs.getInt("transactionType"),
                            rs.getString("transactionTime"),
                            rs.getString("memberID"),
                            rs.getString("goodsID"),
                            rs.getString("goodsName")
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    conn.close()
                }
            }
        }
    } else {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(856.dp).height(544.dp)) {
                Text(
                    text = "主页",
                    fontSize = 32.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "数据库连接失败，请前往设置界面填写数据库地址、用户名及密码，并连接数据库！",
                    fontSize = 18.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
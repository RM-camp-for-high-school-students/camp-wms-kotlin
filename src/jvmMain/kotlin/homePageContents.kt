import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.DriverManager

@Composable
fun homePageContents() {
    var isDatabaseAvailable by remember { mutableStateOf(false) }

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword, currentDatabase)
    if (isDatabaseAvailable) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "物资编号",
                            fontSize = 18.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    Box(modifier = Modifier.width(150.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "物资名称",
                            fontSize = 18.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "物资类型",
                            fontSize = 18.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "总数量",
                            fontSize = 18.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "库存数量",
                            fontSize = 18.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Class.forName("com.mysql.cj.jdbc.Driver")
                    val connection =
                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                    val statement = connection.createStatement()
                    statement.execute("use $currentDatabase")
                    val resultSet = statement.executeQuery(
                        "select T1.goodsID_05, goodsName, Type, count, IFNULL(T2.remain, 0) as remain\n" +
                                "from (select rm_goods.goodsID_05, bom.goodsName, bom.goodsType Type, count(*) as count\n" +
                                "      from rm_goods\n" +
                                "               left join bom on bom.goodsID_05 = rm_goods.goodsID_05\n" +
                                "      GROUP BY rm_goods.goodsID_05) T1\n" +
                                "         left join\n" +
                                "     (select goodsID_05, count(*) as remain from rm_goods where isAvailable = 1 GROUP BY rm_goods.goodsID_05) T2\n" +
                                "     on T1.goodsID_05 = T2.goodsID_05"
                    )
                    var lineCount = 0
                    while (resultSet.next()) {
                        lineCount++
                        val goodsTypeID = resultSet.getString("goodsID_05")
                        val goodsName = resultSet.getString("goodsName")
                        val type = resultSet.getString("Type")
                        val count = resultSet.getInt("count")
                        val remain = resultSet.getInt("remain")
                        val currentLineColor = if (lineCount % 2 == 0) {
                            Color.White
                        } else {
                            lightBlue
                        }
                        var isLineClicked by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier.fillMaxWidth().background(color = currentLineColor)
                                .clickable(onClick = { isLineClicked = !isLineClicked }),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = goodsTypeID,
                                    fontSize = 18.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal,
                                )
                            }
                            Box(modifier = Modifier.width(150.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = goodsName,
                                    fontSize = 18.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = type,
                                    fontSize = 18.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = count.toString(),
                                    fontSize = 18.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = remain.toString(),
                                    fontSize = 18.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                        if (isLineClicked) {
                            val statement = connection.createStatement()
                            statement.execute("use $currentDatabase")
                            val resultSet =
                                statement.executeQuery("select goodsID, isAvailable from rm_goods where goodsID_05 = '$goodsTypeID' order by isAvailable desc, goodsID")
                            while (resultSet.next()) {
                                val goodsID = resultSet.getString("goodsID")
                                val isAvailable = resultSet.getInt("isAvailable")
                                if (isAvailable == 0) {
                                    val statement = connection.createStatement()
                                    statement.execute("use $currentDatabase")
                                    val resultSet =
                                        statement.executeQuery("select memberID, transactionTime from transactions where goodsID = '$goodsID' order by transactionTime desc limit 1")
                                    resultSet.next()
                                    val memberID = resultSet.getString("memberID")
                                    val transactionTime = resultSet.getString("transactionTime")
                                    Text(
                                        text = goodsID + " 已于" + transactionTime + " 被" + memberID + " 借出",
                                        fontSize = 16.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                } else {
                                    Text(
                                        text = goodsID + " 可借用",
                                        fontSize = 16.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    connection.close()
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
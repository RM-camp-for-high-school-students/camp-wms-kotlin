import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.DriverManager

@Preview
@Composable
fun searchContents() {
    var userInputGoodsID by remember { mutableStateOf("") }
    var isGoodsAvailable by remember { mutableStateOf(0) }
    var isGoodsRemoved by remember { mutableStateOf(1) }
    var isGoodsInDatabase by remember { mutableStateOf(false) }
    var isDatabaseAvailable by remember { mutableStateOf(false) }
    var goodsIDFromDatabase by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword)
    if (isDatabaseAvailable) {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "查询", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "物资在库情况查询",
                        fontSize = 24.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "物资编号",
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            textAlign = TextAlign.Start,
                            color = blue,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                        TextField(
                            value = userInputGoodsID,
                            onValueChange = { userInputGoodsID = it },
                            label = null,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = lightBlue,
                                cursorColor = Color.Black,
                                disabledLabelColor = lightBlue,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            trailingIcon = {
                                if (userInputGoodsID.isNotEmpty()) {
                                    IconButton(onClick = { userInputGoodsID = ""; isButtonClicked = false }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close, contentDescription = null
                                        )
                                    }
                                }
                            },
                            textStyle = TextStyle(fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Normal)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isButtonClicked = true
                            Class.forName("com.mysql.cj.jdbc.Driver")
                            val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                            val stmt = conn.createStatement()
                            stmt.execute("use $currentDatabase")
                            if (userInputGoodsID.length == 6) {
                                isGoodsInDatabase = try {
                                    val rs = stmt.executeQuery(
                                        "select *\n" +
                                                "from rm_goods\n" +
                                                "where substring(goodsID from 1 for 6) = '$userInputGoodsID';"
                                    )
                                    rs.next()
                                    rs.getString("goodsName")
                                    true
                                } catch (e: Exception) {
                                    false
                                }
                                isGoodsRemoved = try {
                                    val rs = stmt.executeQuery(
                                        "select *\n" +
                                                "from rm_goods\n" +
                                                "where substring(goodsID from 1 for 6) = '$userInputGoodsID' and isRemoved = 1;"
                                    )
                                    rs.next()
                                    rs.getString("goodsName")
                                    1
                                } catch (e: Exception) {
                                    0
                                }
                                if (isGoodsInDatabase) {
                                    try {
                                        val rs = stmt.executeQuery(
                                            "select *\n" +
                                                    "from rm_goods\n" +
                                                    "where substring(goodsID from 1 for 6) = '$userInputGoodsID' and isRemoved = 0 and isAvailable = 1 limit 1"
                                        )
                                        rs.next()
                                        goodsIDFromDatabase = rs.getString("goodsID")
                                        isGoodsRemoved = 0
                                        isGoodsAvailable = 1
                                    } catch (e: Exception) {
                                        goodsIDFromDatabase = userInputGoodsID + "0001"
                                    }
                                }
                            } else if (userInputGoodsID.length == 10) {
                                isGoodsInDatabase = try {
                                    val rs = stmt.executeQuery(
                                        "select *\n" +
                                                "from rm_goods\n" +
                                                "where goodsID = '$userInputGoodsID';"
                                    )
                                    rs.next()
                                    rs.getString("goodsName")
                                    goodsIDFromDatabase = userInputGoodsID
                                    true
                                } catch (e: Exception) {
                                    goodsIDFromDatabase = ""
                                    false
                                }
                                isGoodsRemoved = try {
                                    val rs = stmt.executeQuery(
                                        "select *\n" +
                                                "from rm_goods\n" +
                                                "where goodsID = '$userInputGoodsID' and isRemoved = 1;"
                                    )
                                    rs.next()
                                    rs.getString("goodsName")
                                    1
                                } catch (e: Exception) {
                                    0
                                }
                                if (isGoodsInDatabase) {
                                    try {
                                        val rs = stmt.executeQuery(
                                            "select *\n" +
                                                    "from rm_goods\n" +
                                                    "where goodsID = '$userInputGoodsID' and isRemoved = 0 and isAvailable = 1 limit 1"
                                        )
                                        rs.next()
                                        goodsIDFromDatabase = rs.getString("goodsID")
                                        isGoodsRemoved = 0
                                        isGoodsAvailable = 1
                                    } catch (e: Exception) {
                                        isGoodsAvailable = 0
                                    }
                                }
                            } else {
                                isDatabaseAvailable = false
                            }
                            conn.close()
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ), colors = ButtonDefaults.buttonColors(
                            backgroundColor = blue,
                        )
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "查询",
                            color = Color.White,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isButtonClicked) {
                        if (!isGoodsInDatabase) {
                            Text(
                                text = "当前所查询的物资不存在或输入格式错误，请重试！",
                                fontSize = 18.sp,
                                fontFamily = HarmonyOS_Sans_SC,
                                fontWeight = FontWeight.Normal
                            )
                        } else {
                            if (userInputGoodsID.length == 6) {
                                if (isGoodsAvailable == 1) {
                                    Text(
                                        text = "当前所查询的物资至少有一件在仓库可供借用，请到物资借出界面进行相关操作！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                } else if (isGoodsRemoved == 1) {
                                    Text(
                                        text = "当前所查询物资至少有一件已从仓库中移除！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                } else {
                                    Text(
                                        text = "当前所查询物资均已借出！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            } else {
                                if (isGoodsAvailable == 1) {
                                    Text(
                                        text = "当前所查询的物资可供借用，请到物资借出界面进行相关操作！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                } else if (isGoodsRemoved == 1) {
                                    Text(
                                        text = "当前所查询物资已从仓库中移除！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                } else {
                                    Text(
                                        text = "当前所查询物资已借出！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isGoodsInDatabase) {
                            Row {
                                Text(
                                    text = "最近操作",
                                    fontSize = 24.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = goodsIDFromDatabase,
                                    fontSize = 24.sp,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontWeight = FontWeight.Light,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                Class.forName("com.mysql.cj.jdbc.Driver")
                                val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                val stmt = conn.createStatement()
                                stmt.execute("use $currentDatabase")
                                val rs = stmt.executeQuery(
                                    "select transactionTime, transactionType, memberID, goodsID, goodsName\n" +
                                            "from transactions\n" +
                                            "         join rm_goods using (goodsID)\n" +
                                            "where goodsID = '$goodsIDFromDatabase'\n" +
                                            "order by transactionTime desc\n" +
                                            "limit 3"
                                )
                                var counter: Int = 0
                                while (rs.next()) {
                                    commonMessageCard(
                                        rs.getInt("transactionType"),
                                        rs.getString("transactionTime"),
                                        rs.getString("memberID"),
                                        rs.getString("goodsID"),
                                        rs.getString("goodsName")
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    counter++
                                }
                                conn.close()
                                if (counter == 0) {
                                    Text(
                                        text = "未查询到该物资的操作记录！",
                                        fontSize = 18.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                    if (userInputGoodsID.length == 5 || userInputGoodsID.length == 9) {
                        isButtonClicked = false
                    }
                }
            }
        }
    } else {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "查询", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
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
}
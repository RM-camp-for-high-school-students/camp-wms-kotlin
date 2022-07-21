import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun returnContents() {
    var isDatabaseAvailable by remember { mutableStateOf(false) }
    var userInputGoodsIDs by remember { mutableStateOf("") }
    var userInputMemberID by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isMemberInDatabase by remember { mutableStateOf(false) }
    var lastGoodsID = ""
    var goodsCount = 0
    var failedTrials = 0

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword)

    if (isDatabaseAvailable) {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "物资归还", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.width(432.dp)) {
                            Text(
                                text = "物资编号",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                textAlign = TextAlign.Start,
                                color = blue,
                                fontFamily = HarmonyOS_Sans_SC,
                                fontWeight = FontWeight.Normal
                            )
                            TextField(
                                value = userInputGoodsIDs,
                                onValueChange = { userInputGoodsIDs = it },
                                label = null,
                                modifier = Modifier.fillMaxWidth().height(89.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = lightBlue,
                                    cursorColor = Color.Black,
                                    disabledLabelColor = lightBlue,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = false,
                                trailingIcon = {
                                    if (userInputGoodsIDs.isNotEmpty()) {
                                        IconButton(onClick = { userInputGoodsIDs = ""; isButtonClicked = false; }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close, contentDescription = null
                                            )
                                        }
                                    }
                                },
                                textStyle = TextStyle(fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Normal)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.width(432.dp)) {
                            Text(
                                text = "经手人编号",
                                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                textAlign = TextAlign.Start,
                                color = blue,
                                fontFamily = HarmonyOS_Sans_SC,
                                fontWeight = FontWeight.Normal
                            )
                            TextField(
                                value = userInputMemberID,
                                onValueChange = { userInputMemberID = it },
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
                                    if (userInputMemberID.isNotEmpty()) {
                                        IconButton(onClick = { userInputMemberID = ""; isButtonClicked = false }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close, contentDescription = null
                                            )
                                        }
                                    }
                                },
                                textStyle = TextStyle(fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Normal)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isButtonClicked = true

                        val goodsIDs = userInputGoodsIDs.split("\r?\n|\r".toRegex()).toTypedArray()
                        failedTrials = 0

                        userInputGoodsIDs = ""

                        if (goodsIDs.isNotEmpty()) {
                            if (goodsIDs.last() == "") {
                                lastGoodsID = goodsIDs[goodsIDs.lastIndex - 1]
                                goodsCount = goodsIDs.count() - 1
                            } else {
                                lastGoodsID = goodsIDs.last()
                                goodsCount = goodsIDs.count()
                            }

                            Class.forName("com.mysql.cj.jdbc.Driver")
                            val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                            val stmt = conn.createStatement()
                            stmt.execute("use $currentDatabase")
                            if (userInputMemberID.isEmpty()) {
                                userInputMemberID = "SP000001"
                            }
                            isMemberInDatabase = try {
                                val rs = stmt.executeQuery(
                                    "select *\n" + "from group_members\n" + "where memberID = '$userInputMemberID';"
                                )
                                rs.next()
                                rs.getString("memberID")
                                true
                            } catch (e: Exception) {
                                false
                            }
                            if (isMemberInDatabase) {
                                for (goodsID in goodsIDs) {
                                    if (goodsID != "") {
                                        var isGoodsRemoved: Int
                                        var isGoodsAvailable: Int
                                        val isGoodsInDatabase = try {
                                            val rs = stmt.executeQuery(
                                                "select *\n" + "from rm_goods\n" + "where goodsID = '$goodsID';"
                                            )
                                            rs.next()
                                            rs.getString("goodsName")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                        if (!isGoodsInDatabase) {
                                            failedTrials++
                                            userInputGoodsIDs += goodsID + "\n"
                                            continue
                                        }
                                        try {
                                            val rs = stmt.executeQuery(
                                                "select *\n" + "from rm_goods\n" + "where goodsID = '$goodsID'"
                                            )
                                            rs.next()
                                            isGoodsRemoved = rs.getInt("isRemoved")
                                            isGoodsAvailable = rs.getInt("isAvailable")
                                        } catch (e: Exception) {
                                            failedTrials++
                                            userInputGoodsIDs += goodsID + "\n"
                                            continue
                                        }
                                        if (isGoodsRemoved == 0 && isGoodsAvailable == 0) {
                                            stmt.execute(
                                                "insert transactions\n" + "(memberID, goodsID, transactionType)\n" + "value\n" + "('$userInputMemberID','$goodsID',0);"
                                            )
                                            stmt.execute(
                                                "update rm_goods\n" + "set isAvailable = 1\n" + "where goodsID = '$goodsID';"
                                            )
                                        } else {
                                            failedTrials++
                                            userInputGoodsIDs += goodsID + "\n"
                                            continue
                                        }
                                    }
                                }
                                conn.close()
                            }
                        } else {
                            goodsCount = 0
                        }
                    }, contentPadding = PaddingValues(
                        start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                    ), colors = ButtonDefaults.buttonColors(
                        backgroundColor = blue,
                    )
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "归还",
                        color = Color.White,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isButtonClicked) {
                    if (!isMemberInDatabase) {
                        Text(
                            text = "数据库中无相关营员信息，请重试！",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    } else {
                        val succeedTrials = goodsCount - failedTrials
                        Text(
                            text = "操作成功，其中$succeedTrials" + "件物资归还成功，$failedTrials" + "件物资归还失败！",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            Class.forName("com.mysql.cj.jdbc.Driver")
                            val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                            val stmt = conn.createStatement()
                            stmt.execute("use $currentDatabase")
                            val rs = stmt.executeQuery(
                                "select transactionTime, transactionType, memberID, goodsID, goodsName\n" + "from transactions\n" + "         join rm_goods using (goodsID)\n" + "where goodsID = '$lastGoodsID'\n" + "order by transactionTime desc\n" + "limit 3"
                            )
                            var counter = 0
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
            }
        }
    } else {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "物资归还", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
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
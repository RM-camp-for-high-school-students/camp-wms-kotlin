import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

@Composable
fun addContents() {
    var isDatabaseAvailable by remember { mutableStateOf(false) }
    var userInputGoodsIDs by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    var failedTrials = 0
    var goodsCount = 0

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword)
    if (isDatabaseAvailable) {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "物资入库", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isButtonClicked = true

                            val goodsIDs = userInputGoodsIDs.split("\r?\n|\r".toRegex()).toTypedArray()
                            failedTrials = 0

                            userInputGoodsIDs = ""

                            Class.forName("com.mysql.cj.jdbc.Driver")
                            val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                            val stmt = conn.createStatement()
                            stmt.execute("use $currentDatabase")

                            if (goodsIDs.isNotEmpty()) {
                                goodsCount = goodsIDs.count()
                                for (goodsID in goodsIDs) {
                                    val goodsID05 = goodsID.substring(0..5)
                                    val isGoodsIDValid = try {
                                        val rs = stmt.executeQuery(
                                            "select goodsID_05 from bom where goodsID_05 = '$goodsID05'"
                                        )
                                        rs.next()
                                        rs.getString("goodsID_05")
                                        true
                                    } catch (e: Exception) {
                                        false
                                    }
                                    if (!isGoodsIDValid) {
                                        failedTrials++
                                        userInputGoodsIDs += goodsID + "\n"
                                        continue
                                    }

                                    val isGoodsInDatabase = try {
                                        val rs = stmt.executeQuery(
                                            "select * from rm_goods where goodsID = '$goodsID'"
                                        )
                                        rs.next()
                                        rs.getString("goodsName")
                                        true
                                    } catch (e: Exception) {
                                        false
                                    }
                                    if (isGoodsInDatabase) {
                                        failedTrials++
                                        userInputGoodsIDs += goodsID + "\n"
                                        continue
                                    }
                                    val rs = stmt.executeQuery(
                                        "select * from bom where goodsID_05 = '$goodsID05'"
                                    )
                                    rs.next()
                                    val goodsName = rs.getString("goodsName")
                                    val goodsType = rs.getString("goodsType")
                                    stmt.execute(
                                        "insert rm_goods\n" +
                                                "(goodsID, goodsID_05, goodsType, goodsName, isRemoved, isAvailable)\n" +
                                                "value\n" +
                                                "('$goodsID', '$goodsID05', '$goodsType', '$goodsName', 0, 1)"
                                    )
                                }
                            } else {
                                goodsCount = 0
                            }
                            conn.close()
                        }, contentPadding = PaddingValues(
                            start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                        ), colors = ButtonDefaults.buttonColors(
                            backgroundColor = blue
                        )
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "入库",
                            color = Color.White,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isButtonClicked) {
                        val succeedTrials = goodsCount - failedTrials
                        Text(
                            text = "操作成功，其中$succeedTrials" + "件物资入库成功，$failedTrials" + "件物资入库失败！",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
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
                        text = "物资入库", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.DriverManager

@Composable
fun searchContents() {
    var isDatabaseAvailable by remember { mutableStateOf(false) }
    var userInputContents by remember { mutableStateOf("") }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isInputValid by remember { mutableStateOf(false) }

    isDatabaseAvailable = testDatabaseConnection(databaseUrl, databaseUserName, databasePassword)
    if (isDatabaseAvailable) {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width(880.dp).height(544.dp)) {
                Column {
                    Text(
                        text = "查询",
                        fontSize = 32.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = userInputContents,
                            onValueChange = { userInputContents = it },
                            label = null,
                            modifier = Modifier.width(766.dp),
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
                                if (userInputContents.isNotEmpty()) {
                                    IconButton(onClick = { userInputContents = ""; isButtonClicked = false; }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close, contentDescription = null
                                        )
                                    }
                                }
                            },
                            textStyle = TextStyle(fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Normal)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                isButtonClicked = true

                                Class.forName("com.mysql.cj.jdbc.Driver")
                                val conn = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                val stmt = conn.createStatement()
                                stmt.execute("use $currentDatabase")

                                when (userInputContents.length) {
                                    8 -> {
                                        isInputValid = try {
                                            val rs =
                                                stmt.executeQuery("select memberID from group_members where memberID = '$userInputContents'")
                                            rs.next()
                                            rs.getString("memberID")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    6 -> {
                                        isInputValid = try {
                                            val rs =
                                                stmt.executeQuery("select goodsName from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 limit 1")
                                            rs.next()
                                            rs.getString("goodsName")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    10 -> {
                                        isInputValid = try {
                                            val rs =
                                                stmt.executeQuery("select goodsName from rm_goods where goodsID = '$userInputContents' and isRemoved = 0")
                                            rs.next()
                                            rs.getString("goodsName")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    else -> {
                                        isInputValid = false
                                    }
                                }
                                conn.close()
                            },
                            contentPadding = PaddingValues(
                                start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = blue
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
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isButtonClicked) {
                        if (!isInputValid) {
                            Text(
                                text = "输入内容有误，请重新输入正确的人员编号、物资编号前六位或完整物资编号！",
                                fontSize = 16.sp,
                                fontFamily = HarmonyOS_Sans_SC,
                                fontWeight = FontWeight.Normal
                            )
                        } else {
                            when (userInputContents.length) {
                                8 -> {
                                    Class.forName("com.mysql.cj.jdbc.Driver")
                                    val conn =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val stmt = conn.createStatement()
                                    stmt.execute("use $currentDatabase")

                                    var rs =
                                        stmt.executeQuery("select groupID from group_members where memberID = '$userInputContents'")
                                    rs.next()
                                    val specificGroupID = rs.getString("groupID")

                                    rs =
                                        stmt.executeQuery("select memberID from group_members where groupID = '$specificGroupID'")
                                    val specificMemberIDs: ArrayList<String> = ArrayList()
                                    specificMemberIDs.add(userInputContents)
                                    while (rs.next()) {
                                        val currentMemberID = rs.getString("memberID")
                                        if (currentMemberID != userInputContents) {
                                            specificMemberIDs.add(currentMemberID)
                                        }
                                    }
                                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                        for (specificMemberID in specificMemberIDs) {
                                            var isContentVisible by remember { mutableStateOf(false) }
                                            Box(
                                                modifier = Modifier.fillMaxWidth().height(32.dp)
                                                    .clickable { isContentVisible = !isContentVisible }
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .background(color = blue)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Icon(
                                                        Icons.Outlined.KeyboardArrowDown,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Text(
                                                        text = specificMemberID,
                                                        fontFamily = HarmonyOS_Sans_SC,
                                                        fontSize = 17.sp,
                                                        fontWeight = FontWeight.Normal,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                            if (isContentVisible) {
                                                Spacer(modifier = Modifier.height(16.dp))
                                                val allBorrowedGoods: ArrayList<Pair<String, String>> = ArrayList()
                                                val borrowedGoodsNotInWarehouse: ArrayList<Pair<String, String>> =
                                                    ArrayList()
                                                val borrowedGoods: ArrayList<Pair<String, String>> = ArrayList()
                                                rs =
                                                    stmt.executeQuery("select goodsID,transactionTime from transactions where memberID = '$specificMemberID' and transactionType = 1 order by transactionTime desc")
                                                while (rs.next()) {
                                                    allBorrowedGoods.add(
                                                        Pair(
                                                            rs.getString("goodsID"),
                                                            rs.getString("transactionTime")
                                                        )
                                                    )
                                                }
                                                for (currentGoods in allBorrowedGoods) {
                                                    val currentGoodsID = currentGoods.first
                                                    rs =
                                                        stmt.executeQuery("select isAvailable from rm_goods where goodsID = '$currentGoodsID'")
                                                    rs.next()
                                                    if (rs.getInt("isAvailable") == 0) {
                                                        borrowedGoodsNotInWarehouse.add(currentGoods)
                                                    }
                                                }
                                                allBorrowedGoods.clear()
                                                for (currentGoods in borrowedGoodsNotInWarehouse) {
                                                    val currentGoodsID = currentGoods.first
                                                    rs = stmt.executeQuery(
                                                        "select memberID from transactions where goodsID = '$currentGoodsID' and transactionType = 1\n" +
                                                                "order by transactionTime desc\n" +
                                                                "limit 1"
                                                    )
                                                    rs.next()
                                                    if (rs.getString("memberID") == specificMemberID) {
                                                        borrowedGoods.add(currentGoods)
                                                    }
                                                }
                                                borrowedGoodsNotInWarehouse.clear()
                                                val goodsIDSet: MutableSet<String> = mutableSetOf("")
                                                for (item in borrowedGoods) {
                                                    val currentGoodsID = item.first
                                                    if (!goodsIDSet.contains(currentGoodsID)) {
                                                        rs =
                                                            stmt.executeQuery("select goodsName from rm_goods where goodsID = '$currentGoodsID'")
                                                        rs.next()
                                                        val currentGoodsName = rs.getString("goodsName")
                                                        Row {
                                                            Text(
                                                                text = item.second,
                                                                fontSize = 16.sp,
                                                                fontFamily = HarmonyOS_Sans_SC,
                                                                fontWeight = FontWeight.Normal
                                                            )
                                                            Spacer(modifier = Modifier.width(16.dp))
                                                            Text(
                                                                text = item.first,
                                                                fontSize = 16.sp,
                                                                fontFamily = HarmonyOS_Sans_SC,
                                                                fontWeight = FontWeight.Normal
                                                            )
                                                            Spacer(modifier = Modifier.width(16.dp))
                                                            Text(
                                                                text = currentGoodsName,
                                                                fontSize = 16.sp,
                                                                fontFamily = HarmonyOS_Sans_SC,
                                                                fontWeight = FontWeight.Normal
                                                            )
                                                        }
                                                        goodsIDSet.add(currentGoodsID)
                                                    }
                                                }
                                                if (borrowedGoods.isEmpty()) {
                                                    Text(
                                                        text = "当前营员未借出任何物资！",
                                                        fontSize = 16.sp,
                                                        fontFamily = HarmonyOS_Sans_SC,
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }
                                    }
                                    conn.close()
                                }
                                6 -> {
                                    Class.forName("com.mysql.cj.jdbc.Driver")
                                    val conn =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val stmt = conn.createStatement()
                                    stmt.execute("use $currentDatabase")
                                    var rs =
                                        stmt.executeQuery("select count(*) from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0")
                                    rs.next()
                                    val goodsCount = rs.getInt("count(*)")
                                    rs =
                                        stmt.executeQuery("select count(*) from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 and isAvailable = 0")
                                    rs.next()
                                    val borrowedCount = rs.getInt("count(*)")
                                    rs =
                                        stmt.executeQuery("select goodsName from rm_goods where goodsID_05 = '$userInputContents' limit 1")
                                    rs.next()
                                    val goodsName = rs.getString("goodsName")
                                    Text(
                                        text = "$userInputContents $goodsName" + "共有$goodsCount" + "件，其中$borrowedCount" + "件已借出。",
                                        fontSize = 16.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (borrowedCount > 0) {
                                        val borrowedGoodsIDs: ArrayList<String> = ArrayList()
                                        rs =
                                            stmt.executeQuery("select goodsID from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 and isAvailable = 0")
                                        while (rs.next()) {
                                            borrowedGoodsIDs.add(rs.getString("goodsID"))
                                        }
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            for (currentGoodsID in borrowedGoodsIDs) {
                                                rs =
                                                    stmt.executeQuery("select memberID, transactionTime from transactions where goodsID = '$currentGoodsID' order by transactionTime desc limit 1")
                                                rs.next()
                                                val currentMemberID = rs.getString("memberID")
                                                val transactionTime = rs.getString("transactionTime")
                                                Text(
                                                    text = currentMemberID + "于$transactionTime" + "借出了$currentGoodsID " + goodsName + "。",
                                                    fontFamily = HarmonyOS_Sans_SC,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                            }
                                        }
                                    }
                                    conn.close()
                                }
                                10 -> {
                                    Class.forName("com.mysql.cj.jdbc.Driver")
                                    val conn =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val stmt = conn.createStatement()
                                    stmt.execute("use $currentDatabase")
                                    var rs =
                                        stmt.executeQuery("select * from rm_goods where goodsID = '$userInputContents'")
                                    rs.next()
                                    val goodsName = rs.getString("goodsName")
                                    val isCurrentGoodsAvailable = rs.getInt("isAvailable")
                                    if (isCurrentGoodsAvailable == 1) {
                                        Text(
                                            text = "$userInputContents $goodsName" + "当前在仓库中可供借用。",
                                            fontSize = 16.sp,
                                            fontFamily = HarmonyOS_Sans_SC,
                                            fontWeight = FontWeight.Normal
                                        )
                                    } else {
                                        rs =
                                            stmt.executeQuery("select * from transactions where goodsID = '$userInputContents' order by transactionTime desc limit 1")
                                        rs.next()
                                        val transactionTime = rs.getString("transactionTime")
                                        val memberID = rs.getString("memberID")
                                        Text(
                                            text = "$userInputContents $goodsName" + "已于$transactionTime" + "由$memberID" + "借出。"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row {
                                        rs = stmt.executeQuery(
                                            "select transactionTime, transactionType, memberID, goodsID, goodsName\n" + "from transactions\n" + "         join rm_goods using (goodsID)\n" + "where goodsID = '$userInputContents'\n" + "order by transactionTime desc\n" + "limit 3"
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
                                        if (counter == 0) {
                                            Text(
                                                text = "未查询到该物资的借还记录！",
                                                fontSize = 18.sp,
                                                fontFamily = HarmonyOS_Sans_SC,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    }
                                    conn.close()
                                }
                            }
                        }
                    }
                    if (userInputContents.length == 5 || userInputContents.length == 7 || userInputContents.length == 9) {
                        isButtonClicked = false
                    }
                }
            }
        }
    } else {
        MaterialTheme {
            Spacer(modifier = Modifier.width(32.dp))
            Column(modifier = Modifier.width((880.dp)).height(544.dp)) {
                Column {
                    Text(
                        text = "查询",
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
}
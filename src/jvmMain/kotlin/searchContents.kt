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
                                val connection = DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                val statement = connection.createStatement()
                                statement.execute("use $currentDatabase")

                                when (userInputContents.length) {
                                    8 -> {
                                        isInputValid = try {
                                            val resultSet =
                                                statement.executeQuery("select memberID from group_members where memberID = '$userInputContents'")
                                            resultSet.next()
                                            resultSet.getString("memberID")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    6 -> {
                                        isInputValid = try {
                                            val resultSet =
                                                statement.executeQuery("select goodsName from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 limit 1")
                                            resultSet.next()
                                            resultSet.getString("goodsName")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    10 -> {
                                        isInputValid = try {
                                            val resultSet =
                                                statement.executeQuery("select goodsName from rm_goods where goodsID = '$userInputContents' and isRemoved = 0")
                                            resultSet.next()
                                            resultSet.getString("goodsName")
                                            true
                                        } catch (e: Exception) {
                                            false
                                        }
                                    }
                                    else -> {
                                        isInputValid = false
                                    }
                                }
                                connection.close()
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
                                    val connection =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val statement = connection.createStatement()
                                    statement.execute("use $currentDatabase")

                                    var resultSet =
                                        statement.executeQuery("select groupID from group_members where memberID = '$userInputContents'")
                                    resultSet.next()
                                    val specificGroupID = resultSet.getString("groupID")

                                    resultSet =
                                        statement.executeQuery("select memberID from group_members where groupID = '$specificGroupID'")
                                    val specificMemberIDs: ArrayList<String> = ArrayList()
                                    specificMemberIDs.add(userInputContents)
                                    while (resultSet.next()) {
                                        val currentMemberID = resultSet.getString("memberID")
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
                                                resultSet =
                                                    statement.executeQuery("select goodsID,transactionTime from transactions where memberID = '$specificMemberID' and transactionType = 1 order by transactionTime desc")
                                                while (resultSet.next()) {
                                                    allBorrowedGoods.add(
                                                        Pair(
                                                            resultSet.getString("goodsID"),
                                                            resultSet.getString("transactionTime")
                                                        )
                                                    )
                                                }
                                                for (currentGoods in allBorrowedGoods) {
                                                    val currentGoodsID = currentGoods.first
                                                    resultSet =
                                                        statement.executeQuery("select isAvailable from rm_goods where goodsID = '$currentGoodsID'")
                                                    resultSet.next()
                                                    if (resultSet.getInt("isAvailable") == 0) {
                                                        borrowedGoodsNotInWarehouse.add(currentGoods)
                                                    }
                                                }
                                                allBorrowedGoods.clear()
                                                for (currentGoods in borrowedGoodsNotInWarehouse) {
                                                    val currentGoodsID = currentGoods.first
                                                    resultSet = statement.executeQuery(
                                                        "select memberID from transactions where goodsID = '$currentGoodsID' and transactionType = 1\n" +
                                                                "order by transactionTime desc\n" +
                                                                "limit 1"
                                                    )
                                                    resultSet.next()
                                                    if (resultSet.getString("memberID") == specificMemberID) {
                                                        borrowedGoods.add(currentGoods)
                                                    }
                                                }
                                                borrowedGoodsNotInWarehouse.clear()
                                                val goodsIDSet: MutableSet<String> = mutableSetOf("")
                                                for (item in borrowedGoods) {
                                                    val currentGoodsID = item.first
                                                    if (!goodsIDSet.contains(currentGoodsID)) {
                                                        resultSet =
                                                            statement.executeQuery("select goodsName from rm_goods where goodsID = '$currentGoodsID'")
                                                        resultSet.next()
                                                        val currentGoodsName = resultSet.getString("goodsName")
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
                                    connection.close()
                                }
                                6 -> {
                                    Class.forName("com.mysql.cj.jdbc.Driver")
                                    val connection =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val statement = connection.createStatement()
                                    statement.execute("use $currentDatabase")
                                    var resultSet =
                                        statement.executeQuery("select count(*) from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0")
                                    resultSet.next()
                                    val goodsCount = resultSet.getInt("count(*)")
                                    resultSet =
                                        statement.executeQuery("select count(*) from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 and isAvailable = 0")
                                    resultSet.next()
                                    val borrowedCount = resultSet.getInt("count(*)")
                                    resultSet =
                                        statement.executeQuery("select goodsName from rm_goods where goodsID_05 = '$userInputContents' limit 1")
                                    resultSet.next()
                                    val goodsName = resultSet.getString("goodsName")
                                    Text(
                                        text = "$userInputContents $goodsName" + "共有$goodsCount" + "件，其中$borrowedCount" + "件已借出。",
                                        fontSize = 16.sp,
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (borrowedCount > 0) {
                                        val borrowedGoodsIDs: ArrayList<String> = ArrayList()
                                        resultSet =
                                            statement.executeQuery("select goodsID from rm_goods where goodsID_05 = '$userInputContents' and isRemoved = 0 and isAvailable = 0")
                                        while (resultSet.next()) {
                                            borrowedGoodsIDs.add(resultSet.getString("goodsID"))
                                        }
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            for (currentGoodsID in borrowedGoodsIDs) {
                                                resultSet =
                                                    statement.executeQuery("select memberID, transactionTime from transactions where goodsID = '$currentGoodsID' order by transactionTime desc limit 1")
                                                resultSet.next()
                                                val currentMemberID = resultSet.getString("memberID")
                                                val transactionTime = resultSet.getString("transactionTime")
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
                                    connection.close()
                                }
                                10 -> {
                                    Class.forName("com.mysql.cj.jdbc.Driver")
                                    val connection =
                                        DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword)
                                    val statement = connection.createStatement()
                                    statement.execute("use $currentDatabase")
                                    var resultSet =
                                        statement.executeQuery("select * from rm_goods where goodsID = '$userInputContents'")
                                    resultSet.next()
                                    val goodsName = resultSet.getString("goodsName")
                                    val isCurrentGoodsAvailable = resultSet.getInt("isAvailable")
                                    if (isCurrentGoodsAvailable == 1) {
                                        Text(
                                            text = "$userInputContents $goodsName" + "当前在仓库中可供借用。",
                                            fontSize = 16.sp,
                                            fontFamily = HarmonyOS_Sans_SC,
                                            fontWeight = FontWeight.Normal
                                        )
                                    } else {
                                        resultSet =
                                            statement.executeQuery("select * from transactions where goodsID = '$userInputContents' order by transactionTime desc limit 1")
                                        resultSet.next()
                                        val transactionTime = resultSet.getString("transactionTime")
                                        val memberID = resultSet.getString("memberID")
                                        Text(
                                            text = "$userInputContents $goodsName" + "已于$transactionTime" + "由$memberID" + "借出。"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row {
                                        resultSet = statement.executeQuery(
                                            "select transactionTime, transactionType, memberID, goodsID, goodsName\n" + "from transactions\n" + "         join rm_goods using (goodsID)\n" + "where goodsID = '$userInputContents'\n" + "order by transactionTime desc\n" + "limit 3"
                                        )
                                        var counter = 0
                                        while (resultSet.next()) {
                                            commonMessageCard(
                                                resultSet.getInt("transactionType"),
                                                resultSet.getString("transactionTime"),
                                                resultSet.getString("memberID"),
                                                resultSet.getString("goodsID"),
                                                resultSet.getString("goodsName")
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
                                    connection.close()
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
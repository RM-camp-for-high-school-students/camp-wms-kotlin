import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun settingsContents() {
    var userInputUserName by remember { mutableStateOf("") }
    var userInputPassword by remember { mutableStateOf("") }

    MaterialTheme {
        Spacer(modifier = Modifier.width(32.dp))
        Column(modifier = Modifier.width(880.dp).height(544.dp)) {
            Column {
                Text(
                    text = "设置", fontSize = 32.sp, fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "数据库连接设置",
                    fontSize = 24.sp,
                    fontFamily = HarmonyOS_Sans_SC,
                    fontWeight = FontWeight.Normal
                )
//                Spacer(modifier = Modifier.height(16.dp))
//                Column {
//                    Text(
//                        text = "数据库地址",
//                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
//                        textAlign = TextAlign.Start,
//                        color = blue,
//                        fontFamily = HarmonyOS_Sans_SC,
//                        fontWeight = FontWeight.Normal
//                    )
//                    TextField(
//                        value = userInputUrl,
//                        onValueChange = { userInputUrl = it },
//                        label = null,
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = TextFieldDefaults.textFieldColors(
//                            backgroundColor = lightBlue,
//                            cursorColor = Color.Black,
//                            disabledLabelColor = lightBlue,
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        ),
//                        shape = RoundedCornerShape(8.dp),
//                        singleLine = true,
//                        trailingIcon = {
//                            if (userInputUrl.isNotEmpty()) {
//                                IconButton(onClick = { userInputUrl = "" }) {
//                                    Icon(
//                                        imageVector = Icons.Outlined.Close, contentDescription = null
//                                    )
//                                }
//                            }
//                        },
//                        textStyle = TextStyle(fontFamily = HarmonyOS_Sans_SC, fontWeight = FontWeight.Normal)
//                    )
//                }
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        text = "用户名",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        textAlign = TextAlign.Start,
                        color = blue,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                    TextField(
                        value = userInputUserName,
                        onValueChange = { userInputUserName = it },
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
                            if (userInputUserName.isNotEmpty()) {
                                IconButton(onClick = { userInputUserName = "" }) {
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
                Column {
                    Text(
                        text = "密码",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        textAlign = TextAlign.Start,
                        color = blue,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                    TextField(
                        value = userInputPassword,
                        onValueChange = { userInputPassword = it },
                        label = null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = lightBlue,
                            cursorColor = Color.Black,
                            disabledLabelColor = lightBlue,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        trailingIcon = {
                            if (userInputPassword.isNotEmpty()) {
                                IconButton(onClick = { userInputPassword = "" }) {
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
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                var testResult by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        testResult = testDatabaseConnection(databaseUrl, userInputUserName, userInputPassword)
                        if (testResult) {
                            databaseUserName = userInputUserName
                            databasePassword = userInputPassword
                            isDatabaseConnected = true
                        }
                    }, contentPadding = PaddingValues(
                        start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp
                    ), colors = ButtonDefaults.buttonColors(
                        backgroundColor = blue,
                    )
                ) {
                    Icon(
                        painterResource("database_black_24dp.svg"),
                        contentDescription = "Test Database Connection",
                        tint = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "连接数据库",
                        color = Color.White,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (isDatabaseConnected || testResult) {
                    Text(
                        text = "数据库连接成功！",
                        fontSize = 16.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                } else {
                    Text(
                        text = "数据库未连接或连接失败，请重新输入数据库地址、用户名及密码！",
                        fontSize = 16.sp,
                        fontFamily = HarmonyOS_Sans_SC,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
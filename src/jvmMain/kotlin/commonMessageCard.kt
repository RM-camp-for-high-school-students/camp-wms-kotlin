import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun commonMessageCard(type: Int, time: String, memberID: String, goodsID: String, goodsName: String) {
    MaterialTheme {
        Card(
            modifier = Modifier.height(185.dp).width(280.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = 2.dp,
            content = {
                Column {
                    Column(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxHeight()) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier.height(40.dp).width(40.dp).clip(RoundedCornerShape(10.dp))
                                    .background(color = blue).padding(4.dp, 4.dp),
                            ) {
                                if (type == 1) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                                    )
                                } else {
                                    Icon(
                                        Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight().align(Alignment.CenterVertically)
                            ) {
                                if (type == 1) {
                                    Text(
                                        text = "借出",
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                } else {
                                    Text(
                                        text = "归还",
                                        fontFamily = HarmonyOS_Sans_SC,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = time,
                                    fontFamily = HarmonyOS_Sans_SC,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "经手人",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        Text(
                            text = memberID,
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "物资编号",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = goodsID,
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "物资名称",
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = goodsName,
                            fontSize = 16.sp,
                            fontFamily = HarmonyOS_Sans_SC,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                }
            }
        )
    }
}
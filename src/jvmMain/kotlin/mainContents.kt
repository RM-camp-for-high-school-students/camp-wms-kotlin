import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun mainContents() {
    var isHomePageVisible by remember { mutableStateOf(true) }
    var isBorrowVisible by remember { mutableStateOf(false) }
    var isReturnVisible by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var isSettingsVisible by remember { mutableStateOf(false) }
    var isAddVisible by remember { mutableStateOf(false) }

    Row {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxHeight().width(64.dp),
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = true
                            isAddVisible = false
                            isBorrowVisible = false
                            isReturnVisible = false
                            isSearchVisible = false
                            isSettingsVisible = false
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Home Page",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = false
                            isAddVisible = true
                            isBorrowVisible = false
                            isReturnVisible = false
                            isSearchVisible = false
                            isSettingsVisible = false
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = false
                            isAddVisible = false
                            isBorrowVisible = true
                            isReturnVisible = false
                            isSearchVisible = false
                            isSettingsVisible = false
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Borrow",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = false
                            isAddVisible = false
                            isBorrowVisible = false
                            isReturnVisible = true
                            isSearchVisible = false
                            isSettingsVisible = false
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Return",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = false
                            isAddVisible = false
                            isBorrowVisible = false
                            isReturnVisible = false
                            isSearchVisible = true
                            isSettingsVisible = false
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(10.dp))
                        .background(color = blue)
                        .clickable {
                            isHomePageVisible = false
                            isAddVisible = false
                            isBorrowVisible = false
                            isReturnVisible = false
                            isSearchVisible = false
                            isSettingsVisible = true
                        }
                        .padding(16.dp, 16.dp)
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp, 48.dp)
                    )
                }
            }
        }
        if (isHomePageVisible)
            homePageContents()
        if (isAddVisible)
            addContents()
        if (isBorrowVisible)
            borrowContents()
        if (isReturnVisible)
            returnContents()
        if (isSearchVisible)
            searchContents()
        if (isSettingsVisible)
            settingsContents()
    }
}
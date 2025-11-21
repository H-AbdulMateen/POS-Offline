package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CartBadgeBox(
    itemCount: Int,
    onCartClick: () -> Unit
){
    BadgedBox(
        badge = {
            if (itemCount > 0) {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("$itemCount")
                }
            }
        },
        modifier = Modifier.clickable(
            onClick = onCartClick
        )
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Shopping cart",
        )
    }
}

@Preview(name = "Light Mode")
@Composable
fun CartBadgeBoxPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CartBadgeBox(
                    itemCount = 5,
                    onCartClick = {}
                )
            }
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun CartBadgeBoxDarkPreview() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                CartBadgeBox(
                    itemCount = 5,
                    onCartClick = {}
                )
            }
        }
    )
}
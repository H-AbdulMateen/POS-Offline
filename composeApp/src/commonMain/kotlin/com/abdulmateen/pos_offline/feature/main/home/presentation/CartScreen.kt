package com.abdulmateen.pos_offline.feature.main.home.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                },
                title = {
                    Text(text = "Cart")
                }
            )
        }
    ) {
        CartSummarySection()
    }
}

@Preview(name = "Light Mode")
@Composable
fun CartScreenPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CartScreen()
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
fun CartScreenDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CartScreen()
        }
    )
}
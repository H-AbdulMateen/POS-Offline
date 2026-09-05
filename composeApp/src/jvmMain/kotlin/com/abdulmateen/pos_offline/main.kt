package com.abdulmateen.pos_offline

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.tally_trades_logo
import com.abdulmateen.pos_offline.di.initKoin

fun main(){
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "POS Offline",
            icon = painterResource(Res.drawable.tally_trades_logo)
        ) {
            App()
        }
    }
}
package com.abdulmateen.pos_offline

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.abdulmateen.pos_offline.di.initKoin

fun main(){
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "POS Offline",
        ) {
            App()
        }
    }
}
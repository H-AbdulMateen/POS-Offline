package com.abdulmateen.cmpskeleton

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.abdulmateen.cmpskeleton.di.initKoin

fun main(){
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "CMPSkeleton",
        ) {
            App()
        }
    }
}
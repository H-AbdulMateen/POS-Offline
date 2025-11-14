package com.abdulmateen.pos_offline

import androidx.compose.ui.window.ComposeUIViewController
import com.abdulmateen.pos_offline.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
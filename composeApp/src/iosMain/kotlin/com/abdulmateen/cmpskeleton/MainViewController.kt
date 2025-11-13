package com.abdulmateen.cmpskeleton

import androidx.compose.ui.window.ComposeUIViewController
import com.abdulmateen.cmpskeleton.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
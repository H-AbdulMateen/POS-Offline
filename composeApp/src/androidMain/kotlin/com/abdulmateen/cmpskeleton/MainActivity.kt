package com.abdulmateen.cmpskeleton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var shouldShowSplashScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            this.setKeepOnScreenCondition { shouldShowSplashScreen }
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App(
                onAuthenticationChecked = {
                    shouldShowSplashScreen = false
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
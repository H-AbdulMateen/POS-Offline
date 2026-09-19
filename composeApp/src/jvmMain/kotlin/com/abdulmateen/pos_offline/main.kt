package com.abdulmateen.pos_offline

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.painterResource
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.compose_multiplatform
import com.abdulmateen.pos_offline.di.initKoin
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
import org.jetbrains.compose.resources.stringResource
import pos_offline.composeapp.generated.resources.app_name
import pos_offline.composeapp.generated.resources.tally_trades_logo
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime

fun main() {
    setupLogging()

    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        logError(e)
    }

    try {
        Logger.d("App: Initializing Koin...")
        initKoin()
        Logger.d("App: Launching application UI...")
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = stringResource(Res.string.app_name),
                icon = painterResource(Res.drawable.tally_trades_logo),
                state = rememberWindowState(placement = WindowPlacement.Maximized)
            ) {
                App()
            }
        }
    } catch (e: Exception) {
        logError(e)
        throw e
    }
}

private fun setupLogging() {
    try {
        val logFile = File(System.getProperty("user.home"), "pos_offline_crash.log")
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
        
        Logger.addLogWriter(object : LogWriter() {
            override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
                val timestamp = LocalDateTime.now()
                val logMessage = "[$timestamp] [$severity] [$tag] $message"
                println(logMessage)
                try {
                    logFile.appendText("$logMessage\n")
                } catch (e: Exception) {
                    // ignore
                }
                throwable?.let {
                    val sw = StringWriter()
                    it.printStackTrace(PrintWriter(sw))
                    val stackTrace = sw.toString()
                    println(stackTrace)
                    try {
                        logFile.appendText("$stackTrace\n")
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        })
    } catch (e: Exception) {
        println("Warning: Could not setup file logging: ${e.message}")
        Logger.addLogWriter(object : LogWriter() {
            override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
                println("[$severity] [$tag] $message")
                throwable?.printStackTrace()
            }
        })
    }
}

private fun logError(e: Throwable) {
    Logger.e("Uncaught Exception", e)
}

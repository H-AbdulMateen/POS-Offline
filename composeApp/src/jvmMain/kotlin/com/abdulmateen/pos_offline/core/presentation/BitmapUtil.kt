package com.abdulmateen.pos_offline.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

@Composable
actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? {
    return remember(bytes) {
        bytes?.let {
            Image.makeFromEncoded(it).toComposeImageBitmap()
        }
    }
}
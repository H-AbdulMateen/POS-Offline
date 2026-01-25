package com.abdulmateen.pos_offline.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap


@Composable
expect fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap?
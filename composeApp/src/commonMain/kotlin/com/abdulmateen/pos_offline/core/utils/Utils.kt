package com.abdulmateen.pos_offline.core.utils

import coil3.Bitmap
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
expect fun formatDatePlatform(date: Instant, outputPattern: String): String

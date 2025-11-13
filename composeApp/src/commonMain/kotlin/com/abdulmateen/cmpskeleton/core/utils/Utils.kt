package com.abdulmateen.cmpskeleton.core.utils

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
expect fun formatDatePlatform(date: Instant, outputPattern: String): String

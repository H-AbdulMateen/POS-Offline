package com.abdulmateen.cmpskeleton.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(markerClass = [ExperimentalTime::class])
actual fun formatDatePlatform(date: Instant, outputPattern: String): String {
    val formatter = SimpleDateFormat(outputPattern, Locale.getDefault())
    val dateInLocalTimeZone = Date(date.toEpochMilliseconds())
    return formatter.format(dateInLocalTimeZone)
}
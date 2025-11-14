package com.abdulmateen.pos_offline.core.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.localTimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(markerClass = [ExperimentalTime::class])
actual fun formatDatePlatform(date: Instant, outputPattern: String): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = outputPattern
    formatter.timeZone = NSTimeZone.localTimeZone
    return formatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(date.epochSeconds.toDouble()))
}
package com.abdulmateen.pos_offline

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val os: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()
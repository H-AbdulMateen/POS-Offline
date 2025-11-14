package com.abdulmateen.pos_offline

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val os: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()
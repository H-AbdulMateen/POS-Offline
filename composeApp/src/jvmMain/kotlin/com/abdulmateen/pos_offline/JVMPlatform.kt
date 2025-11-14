package com.abdulmateen.pos_offline

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val os: String = "Desktop"
}

actual fun getPlatform(): Platform = JVMPlatform()
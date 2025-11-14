package com.abdulmateen.pos_offline
interface Platform {
    val name: String
    val os: String
}

expect fun getPlatform(): Platform
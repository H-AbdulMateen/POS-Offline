package com.abdulmateen.cmpskeleton
interface Platform {
    val name: String
    val os: String
}

expect fun getPlatform(): Platform
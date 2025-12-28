package com.abdulmateen.pos_offline.core.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        BAD_REQUEST,
        FORBIDDEN,
        CONFLICT,
        REQUEST_TIMEOUT,
        PAYLOAD_TOO_LARGE,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        SERVICE_UNAVAILABLE,
        UNKNOWN,
        UNAUTHORIZED
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
        SKU_ALREADY_EXISTS,
        BARCODE_ALREADY_EXISTS
    }
}
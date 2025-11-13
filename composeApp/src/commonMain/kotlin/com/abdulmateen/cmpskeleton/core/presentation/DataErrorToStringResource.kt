package com.abdulmateen.cmpskeleton.core.presentation

import cmpskeleton.composeapp.generated.resources.Res
import cmpskeleton.composeapp.generated.resources.bad_request
import cmpskeleton.composeapp.generated.resources.error_conflict
import cmpskeleton.composeapp.generated.resources.error_disk_full
import cmpskeleton.composeapp.generated.resources.error_forbidden
import cmpskeleton.composeapp.generated.resources.error_no_internet
import cmpskeleton.composeapp.generated.resources.error_payload_too_large
import cmpskeleton.composeapp.generated.resources.error_request_timeout
import cmpskeleton.composeapp.generated.resources.error_serialization
import cmpskeleton.composeapp.generated.resources.error_server
import cmpskeleton.composeapp.generated.resources.error_too_many_requests
import cmpskeleton.composeapp.generated.resources.error_unauthorized
import cmpskeleton.composeapp.generated.resources.error_unknown
import com.abdulmateen.cmpskeleton.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
        DataError.Remote.BAD_REQUEST -> Res.string.bad_request
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.FORBIDDEN -> Res.string.error_forbidden
        DataError.Remote.CONFLICT -> Res.string.error_conflict
        DataError.Remote.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        DataError.Remote.SERVICE_UNAVAILABLE -> Res.string.error_server
    }

    return UiText.StringResourceId(stringRes)
}
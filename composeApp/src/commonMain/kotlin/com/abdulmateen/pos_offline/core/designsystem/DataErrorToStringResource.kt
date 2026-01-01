package com.abdulmateen.pos_offline.core.designsystem

import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.bad_request
import pos_offline.composeapp.generated.resources.error_conflict
import pos_offline.composeapp.generated.resources.error_disk_full
import pos_offline.composeapp.generated.resources.error_forbidden
import pos_offline.composeapp.generated.resources.error_no_internet
import pos_offline.composeapp.generated.resources.error_payload_too_large
import pos_offline.composeapp.generated.resources.error_request_timeout
import pos_offline.composeapp.generated.resources.error_serialization
import pos_offline.composeapp.generated.resources.error_server
import pos_offline.composeapp.generated.resources.error_too_many_requests
import pos_offline.composeapp.generated.resources.error_unauthorized
import pos_offline.composeapp.generated.resources.error_unknown
import com.abdulmateen.pos_offline.core.domain.DataError
import pos_offline.composeapp.generated.resources.error_barcode_already_exists
import pos_offline.composeapp.generated.resources.error_category_already_exists
import pos_offline.composeapp.generated.resources.error_sku_already_exists
import pos_offline.composeapp.generated.resources.error_unit_already_exists

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Local.SKU_ALREADY_EXISTS -> Res.string.error_sku_already_exists
        DataError.Local.BARCODE_ALREADY_EXISTS -> Res.string.error_barcode_already_exists
        DataError.Local.CATEGORY_ALREADY_EXISTS -> Res.string.error_category_already_exists
        DataError.Local.UNIT_ALREADY_EXISTS -> Res.string.error_unit_already_exists

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
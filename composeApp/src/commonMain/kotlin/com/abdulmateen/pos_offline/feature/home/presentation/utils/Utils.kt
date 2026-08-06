package com.abdulmateen.pos_offline.feature.home.presentation.utils

import com.abdulmateen.pos_offline.domain.models.CartItem

expect fun generateInvoiceInPdf(
    cartItems: List<CartItem>,
    subTotal: Double,
    discount: Double,
    tax: Double,
    total: Double,
    paidAmount: Double,
    change: Double,
    paymentType: String
): ByteArray

expect fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String)

expect fun printPdf(invoiceByteArray: ByteArray, fileName: String)

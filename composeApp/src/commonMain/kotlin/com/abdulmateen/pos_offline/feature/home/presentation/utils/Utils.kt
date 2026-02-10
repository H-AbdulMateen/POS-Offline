package com.abdulmateen.pos_offline.feature.home.presentation.utils

expect fun generateInvoiceInPdf(): ByteArray
expect fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String)
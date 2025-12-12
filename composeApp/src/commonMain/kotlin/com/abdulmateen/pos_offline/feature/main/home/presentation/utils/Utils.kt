package com.abdulmateen.pos_offline.feature.main.home.presentation.utils

expect fun generateInvoiceInPdf(): ByteArray
expect fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String)
package com.abdulmateen.pos_offline.feature.main.home.presentation.utils

import com.abdulmateen.pos_offline.MyApplication
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.koin.androidContext

import java.io.File
actual fun generateInvoiceInPdf(): ByteArray {
    val pdfDocument = android.graphics.pdf.PdfDocument()
    val pageInfo =
        android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = android.graphics.Paint()
    paint.color = android.graphics.Color.BLACK
    paint.textSize = 12f

    canvas.drawText("Invoice", 100f, 50f, paint)
    canvas.drawText("Order #123", 100f, 70f, paint)

    canvas.drawText("Customer ID: #", 100f, 100f, paint)

    canvas.drawText("Order Date:", 100f, 130f, paint)
    canvas.drawText("11/12/2025", 200f, 130f, paint)
    canvas.drawText("Delivery Date:", 100f, 150f, paint)
    canvas.drawText("11/12/2025", 200f, 150f, paint)

    canvas.drawText("Product", 100f, 180f, paint)
    canvas.drawText("Quantity", 250f, 180f, paint)
    canvas.drawText("Price", 350f, 180f, paint)
    var yPosition = 200f
    canvas.drawText("Product 1", 100f, yPosition, paint)
    canvas.drawText("100", 250f, yPosition, paint)
    canvas.drawText("5000 pkr", 350f, yPosition, paint)
    yPosition += 20f

    canvas.drawText("Total:", 350f, yPosition + 20f, paint)
    canvas.drawText("5000 pkr", 400f, yPosition + 20f, paint)

    pdfDocument.finishPage(page)

    val outputStream = java.io.ByteArrayOutputStream()
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()

    return outputStream.toByteArray()
}

actual fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String) {
    val context = MyApplication.instance.applicationContext
    val file = File(context.getExternalFilesDir(null), fileName)
    file.writeBytes(invoiceByteArray)
    val uri = androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
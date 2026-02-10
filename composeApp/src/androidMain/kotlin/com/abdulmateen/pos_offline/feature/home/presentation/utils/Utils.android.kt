package com.abdulmateen.pos_offline.feature.home.presentation.utils

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.abdulmateen.pos_offline.MyApplication
import com.abdulmateen.pos_offline.feature.home.presentation.PdfViewerActivity
import java.io.ByteArrayOutputStream
import java.io.File

actual fun generateInvoiceInPdf(): ByteArray {
    val pdfDocument = PdfDocument()
    val pageInfo =
        PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()
    paint.color = Color.BLACK
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

    val outputStream = ByteArrayOutputStream()
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()

    return outputStream.toByteArray()
}

actual fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String) {
    val context = MyApplication.instance.applicationContext
    val file = File(context.getExternalFilesDir(null), fileName)
    file.writeBytes(invoiceByteArray)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        setDataAndType(uri, "application/pdf")
//        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    }
//    context.startActivity(intent)
    context.startActivity(
        Intent(context, PdfViewerActivity::class.java)
            .putExtra("pdf_uri", uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )

}
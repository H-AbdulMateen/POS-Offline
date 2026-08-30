package com.abdulmateen.pos_offline.feature.home.presentation.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.core.content.FileProvider
import com.abdulmateen.pos_offline.MyApplication
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.feature.home.presentation.PdfViewerActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

actual fun generateInvoiceInPdf(
    cartItems: List<CartItem>,
    subTotal: Double,
    discount: Double,
    tax: Double,
    total: Double,
    paidAmount: Double,
    change: Double,
    paymentType: String
): ByteArray {
    val pdfDocument = PdfDocument()
    val pageInfo =
        PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()
    paint.color = Color.BLACK
    paint.textSize = 12f

    canvas.drawText("Invoice", 100f, 50f, paint)
    canvas.drawText("Payment Type: $paymentType", 100f, 70f, paint)

    canvas.drawText("Order Date:", 100f, 100f, paint)
    canvas.drawText(java.util.Date().toString(), 200f, 100f, paint)

    paint.isFakeBoldText = true
    canvas.drawText("Product", 100f, 130f, paint)
    canvas.drawText("Qty", 300f, 130f, paint)
    canvas.drawText("Price", 350f, 130f, paint)
    canvas.drawText("Total", 450f, 130f, paint)
    paint.isFakeBoldText = false

    var yPosition = 150f
    cartItems.forEach { item ->
        canvas.drawText(item.productName, 100f, yPosition, paint)
        canvas.drawText(item.quantity.toString(), 300f, yPosition, paint)
        canvas.drawText("%.2f".format(item.unitPrice), 350f, yPosition, paint)
        canvas.drawText("%.2f".format(item.price), 450f, yPosition, paint)
        yPosition += 20f
    }

    yPosition += 20f
    canvas.drawText("Sub Total:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(subTotal), 450f, yPosition, paint)
    yPosition += 20f
    canvas.drawText("Discount:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(discount), 450f, yPosition, paint)
    yPosition += 20f
    canvas.drawText("Tax:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(tax), 450f, yPosition, paint)
    yPosition += 20f
    
    paint.isFakeBoldText = true
    canvas.drawText("Total:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(total), 450f, yPosition, paint)
    paint.isFakeBoldText = false
    yPosition += 20f
    
    canvas.drawText("Paid Amount:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(paidAmount), 450f, yPosition, paint)
    yPosition += 20f
    canvas.drawText("Change:", 350f, yPosition, paint)
    canvas.drawText("%.2f".format(change), 450f, yPosition, paint)

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
    context.startActivity(
        Intent(context, PdfViewerActivity::class.java)
            .putExtra("pdf_uri", uri)
            .putExtra("pdf_bytes", invoiceByteArray)
            .putExtra("file_name", fileName)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}

actual fun printPdf(invoiceByteArray: ByteArray, fileName: String) {
    val context = MyApplication.instance.applicationContext
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
    val printAdapter = PdfDocumentAdapter(invoiceByteArray, fileName)
    printManager.print(fileName, printAdapter, PrintAttributes.Builder().build())
}

actual fun isPrinterAvailable(): Boolean {
    // In Android, printing is handled by the system print spooler, 
    // which is always available if the device supports it.
    // A more thorough check could be done via PrintManager.
    return true
}

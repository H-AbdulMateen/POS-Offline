package com.abdulmateen.pos_offline.feature.home.presentation.utils

import com.abdulmateen.pos_offline.domain.models.CartItem
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.apache.pdfbox.printing.PDFPageable
import org.apache.pdfbox.printing.PDFPrintable
import org.apache.pdfbox.printing.Scaling
import java.awt.print.PrinterJob
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Date
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.standard.Sides

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
    val document = PDDocument()
    val page = PDPage()
    document.addPage(page)

    val contentStream = PDPageContentStream(document, page)
    contentStream.beginText()
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12f)
    contentStream.newLineAtOffset(100f, 750f)
    contentStream.showText("Invoice")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
    contentStream.showText("Payment Type: $paymentType")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Order Date: ${Date()}")
    contentStream.newLineAtOffset(0f, -20f)
    
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12f)
    contentStream.showText("Product")
    contentStream.newLineAtOffset(200f, 0f)
    contentStream.showText("Qty")
    contentStream.newLineAtOffset(50f, 0f)
    contentStream.showText("Price")
    contentStream.newLineAtOffset(100f, 0f)
    contentStream.showText("Total")
    contentStream.newLineAtOffset(-350f, -20f)
    
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
    cartItems.forEach { item ->
        contentStream.showText(item.productName)
        contentStream.newLineAtOffset(200f, 0f)
        contentStream.showText(item.quantity.toString())
        contentStream.newLineAtOffset(50f, 0f)
        contentStream.showText("%.2f".format(item.unitPrice))
        contentStream.newLineAtOffset(100f, 0f)
        contentStream.showText("%.2f".format(item.price))
        contentStream.newLineAtOffset(-350f, -20f)
    }
    
    contentStream.newLineAtOffset(250f, -20f)
    contentStream.showText("Sub Total: %.2f".format(subTotal))
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Discount: %.2f".format(discount))
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Tax: %.2f".format(tax))
    contentStream.newLineAtOffset(0f, -20f)
    
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12f)
    contentStream.showText("Total: %.2f".format(total))
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
    contentStream.showText("Paid: %.2f".format(paidAmount))
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Change: %.2f".format(change))
    
    contentStream.endText()
    contentStream.close()

    val outputStream = ByteArrayOutputStream()
    document.save(outputStream)
    document.close()

    return outputStream.toByteArray()
}

actual fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String) {
    val file = File(System.getProperty("user.home"), fileName)
    file.writeBytes(invoiceByteArray)
    if (java.awt.Desktop.isDesktopSupported()) {
        java.awt.Desktop.getDesktop().open(file)
    }
}

actual fun printPdf(invoiceByteArray: ByteArray, fileName: String) {
    val document = Loader.loadPDF(invoiceByteArray)
    val job = PrinterJob.getPrinterJob()
    job.setPageable(PDFPageable(document))
    
    val attr = HashPrintRequestAttributeSet()
    attr.add(Sides.ONE_SIDED)
    
    if (job.printDialog(attr)) {
        job.print(attr)
    }
    document.close()
}

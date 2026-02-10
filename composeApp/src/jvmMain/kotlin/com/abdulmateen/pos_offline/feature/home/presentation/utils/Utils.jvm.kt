package com.abdulmateen.pos_offline.feature.home.presentation.utils

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.io.ByteArrayOutputStream
import java.io.File
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
actual fun generateInvoiceInPdf(): ByteArray {
    val document = PDDocument()
    val page = PDPage()
    document.addPage(page)

    val contentStream = PDPageContentStream(document, page)
    contentStream.beginText()
    contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),12f)
    contentStream.newLineAtOffset(100f, 750f)
    contentStream.showText("Invoice")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Order # 12345")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Customer ID: #12345")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Order Date: 11-12-2025")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Delivery Date: 21/07/2024")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Product: Product Name")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Quantity: ${10}")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Price: $200")
    contentStream.newLineAtOffset(0f, -20f)
    contentStream.showText("Total: $200")
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
    java.awt.Desktop.getDesktop().open(file)
}
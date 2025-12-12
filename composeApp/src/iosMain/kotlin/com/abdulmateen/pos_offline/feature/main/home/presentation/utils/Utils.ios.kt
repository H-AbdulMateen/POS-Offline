package com.abdulmateen.pos_offline.feature.main.home.presentation.utils
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSMutableData
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSParagraphStyleAttributeName
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.UIApplication
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsBeginPDFContextToData
import platform.UIKit.UIGraphicsBeginPDFPageWithInfo
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun generateInvoiceInPdf(): ByteArray {
    val pdfData = NSMutableData()
    val pageSize = CGRectMake(0.0, 0.0, 612.0, 792.0)

    UIGraphicsBeginPDFContextToData(pdfData, pageSize, null)
    UIGraphicsBeginPDFPageWithInfo(pageSize, null)

    val context = UIGraphicsGetCurrentContext()

    val text = """
        Invoice
        Order #$12345
        Customer ID: 12
        Order Date: 11/12/2025
        Delivery Date: 11/12/2025
        Products: 123
        Quantity: 10
        Total Price: 100
    """.trimIndent()

    val paragraphStyle = NSMutableParagraphStyle().apply { NSTextAlignmentLeft }
    val attributes: Map<Any?, Any?> = mapOf(
        NSFontAttributeName to UIFont.systemFontOfSize(12.0),
        NSParagraphStyleAttributeName to paragraphStyle
    )
    UIGraphicsEndPDFContext()

    return pdfData.bytes.let {
        it?.reinterpret<ByteVar>()!!.readBytes(pdfData.length.toInt())
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun saveInvoiceFile(invoiceByteArray: ByteArray, fileName: String) {
    val fileManager = NSFileManager.defaultManager
    val documentsDirectory = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).firstOrNull()

    val documentPath = documentsDirectory

    val nsData = invoiceByteArray.usePinned { pinnedData ->
        NSData.create(bytes = pinnedData.addressOf(0), length = invoiceByteArray.size.toULong())
    }

    try {
        nsData.writeToFile(documentPath.toString(), true)
        documentPath.toString()
    } catch (e: Exception){
        "Failed to save PDF: ${e.message}"
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    return this.bytes?.let { bytesPointer ->
        ByteArray(this.length.toInt()).apply {
            usePinned { pinned ->
                memcpy(pinned.addressOf(0), bytesPointer, this@toByteArray.length)
            }
        }
    } ?: ByteArray(0)
}
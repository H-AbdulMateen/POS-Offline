package com.abdulmateen.pos_offline.feature.home.presentation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abdulmateen.pos_offline.feature.home.presentation.utils.PdfViewerScreen

class PdfViewerActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("pdf_uri", Uri::class.java)
        } else {
            intent.getParcelableExtra("pdf_uri")
        }
        val pdfBytes = intent.getByteArrayExtra("pdf_bytes")
        val fileName = intent.getStringExtra("file_name") ?: "invoice.pdf"

        setContent {
            uri?.let {
                PdfViewerScreen(
                    pdfUri = it,
                    pdfBytes = pdfBytes,
                    fileName = fileName
                )
            }
        }
    }
}
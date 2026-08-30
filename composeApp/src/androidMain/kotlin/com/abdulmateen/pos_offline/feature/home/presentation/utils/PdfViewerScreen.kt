package com.abdulmateen.pos_offline.feature.home.presentation.utils

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    pdfUri: Uri,
    pdfBytes: ByteArray? = null,
    fileName: String = "invoice.pdf"
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context)
    }
    var renderedPages by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }
    var showNoPrinterDialog by remember { mutableStateOf(false) }

    if (showNoPrinterDialog) {
        AlertDialog(
            onDismissRequest = { showNoPrinterDialog = false },
            confirmButton = {
                TextButton(onClick = { showNoPrinterDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("No Printer Found") },
            text = { Text("There is no printer available to print the invoice.") }
        )
    }

    LaunchedEffect(pdfUri) {
        pdfUri.let { uri ->
            renderedPages = pdfBitmapConverter.pdfToBitmaps(uri)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Invoice Preview") })
        },
        floatingActionButton = {
            if (pdfBytes != null) {
                FloatingActionButton(onClick = {
                    if (isPrinterAvailable()) {
                        printPdf(pdfBytes, fileName)
                    } else {
                        showNoPrinterDialog = true
                    }
                }) {
                    Icon(Icons.Default.Print, contentDescription = "Print")
                }
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(renderedPages) { index, page ->
                    PdfPage(
                        page = page
                    )
                }
            }
        }
    }
}

@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
            .drawWithContent {
                drawContent()
            }
    )
}

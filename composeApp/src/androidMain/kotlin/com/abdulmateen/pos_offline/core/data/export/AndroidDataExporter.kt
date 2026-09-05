package com.abdulmateen.pos_offline.core.data.export

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.abdulmateen.pos_offline.core.domain.export.BaseDataExporter
import java.io.OutputStream

class AndroidDataExporter(private val context: Context) : BaseDataExporter() {
    override suspend fun exportToCsv(filename: String, headers: List<String>, data: List<List<String>>): String? {
        val csvContent = generateCsvString(headers, data)
        val resolver = context.contentResolver
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$filename.csv")
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream: OutputStream ->
                    outputStream.write(csvContent.toByteArray())
                }
                "Downloads/$filename.csv"
            }
        } else {
            // Older Android versions
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(downloadDir, "$filename.csv")
            file.writeBytes(csvContent.toByteArray())
            file.absolutePath
        }
    }
}

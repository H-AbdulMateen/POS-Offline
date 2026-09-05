package com.abdulmateen.pos_offline.core.data.export

import com.abdulmateen.pos_offline.core.domain.export.BaseDataExporter
import java.io.File

class JvmDataExporter : BaseDataExporter() {
    override suspend fun exportToCsv(filename: String, headers: List<String>, data: List<List<String>>): String? {
        return try {
            val csvContent = generateCsvString(headers, data)
            val downloadDir = File(System.getProperty("user.home"), "Downloads")
            if (!downloadDir.exists()) downloadDir.mkdirs()
            
            val file = File(downloadDir, "$filename.csv")
            file.writeText(csvContent)
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

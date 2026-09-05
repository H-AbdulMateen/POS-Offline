package com.abdulmateen.pos_offline.core.domain.export

abstract class BaseDataExporter : DataExporter {
    protected fun generateCsvString(headers: List<String>, data: List<List<String>>): String {
        val builder = StringBuilder()
        builder.append(headers.joinToString(",") { escapeCsv(it) })
        builder.append("\n")
        data.forEach { row ->
            builder.append(row.joinToString(",") { escapeCsv(it) })
            builder.append("\n")
        }
        return builder.toString()
    }

    private fun escapeCsv(value: String): String {
        val needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n")
        return if (needsQuotes) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }

    override suspend fun exportToExcel(filename: String, headers: List<String>, data: List<List<String>>): String? {
        // For now, we will just export to CSV but with .xls extension or just use CSV as a placeholder
        // Actual Excel generation usually requires platform-specific libraries or a complex multiplatform one.
        // We'll focus on CSV first.
        return exportToCsv(filename, headers, data)
    }
}

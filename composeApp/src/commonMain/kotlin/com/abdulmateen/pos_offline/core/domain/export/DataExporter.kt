package com.abdulmateen.pos_offline.core.domain.export

interface DataExporter {
    suspend fun exportToCsv(filename: String, headers: List<String>, data: List<List<String>>): String?
    suspend fun exportToExcel(filename: String, headers: List<String>, data: List<List<String>>): String?
}

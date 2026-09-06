package com.abdulmateen.pos_offline.domain.repository

interface ReportRepository {
    suspend fun exportSalesReport(startDate: Long, endDate: Long): String?
    suspend fun exportExpenseReport(startDate: Long, endDate: Long): String?
}

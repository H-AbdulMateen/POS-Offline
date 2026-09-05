package com.abdulmateen.pos_offline.domain.repository

interface ReportRepository {
    suspend fun exportSalesReport(): String?
    suspend fun exportExpenseReport(): String?
}

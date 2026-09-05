package com.abdulmateen.pos_offline.data.repository

import com.abdulmateen.pos_offline.core.domain.export.DataExporter
import com.abdulmateen.pos_offline.data.database.dao.ExpenseDao
import com.abdulmateen.pos_offline.data.database.dao.OrderDao
import com.abdulmateen.pos_offline.domain.repository.ReportRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

class ReportRepositoryImpl(
    private val orderDao: OrderDao,
    private val expenseDao: ExpenseDao,
    private val dataExporter: DataExporter
) : ReportRepository {

    override suspend fun exportSalesReport(): String? {
        val orders = orderDao.getAllOrders().first()
        val headers = listOf("Order ID", "Date", "Customer", "Subtotal", "Discount", "Tax", "Total", "Payment Method")
        val data = orders.map { order ->
            val date = Instant.fromEpochMilliseconds(order.createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .let { "${it.day.toString().padStart(2, '0')}-${it.month.number.toString().padStart(2, '0')}-${it.year}" }
            
            listOf(
                order.orderId.toString(),
                date,
                order.customerName ?: "Walk-in",
                order.subTotal.toString(),
                (order.discount ?: 0.0).toString(),
                (order.tax ?: 0.0).toString(),
                order.total.toString(),
                order.paymentMethod
            )
        }
        val timestamp = Clock.System.now().toEpochMilliseconds()
        return dataExporter.exportToCsv("sales_report_$timestamp", headers, data)
    }

    override suspend fun exportExpenseReport(): String? {
        val expenses = expenseDao.getAllExpenses().first()
        val headers = listOf("Expense ID", "Date", "Paid To", "Category", "Amount", "Description")
        val data = expenses.map { expense ->
            val date = Instant.fromEpochMilliseconds(expense.date)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .let { "${it.day.toString().padStart(2, '0')}-${it.month.number.toString().padStart(2, '0')}-${it.year}" }

            listOf(
                expense.expenseId.toString(),
                date,
                expense.paidTo ?: "-",
                expense.category.name,
                expense.amount.toString(),
                expense.description
            )
        }
        val timestamp = Clock.System.now().toEpochMilliseconds()
        return dataExporter.exportToCsv("expense_report_$timestamp", headers, data)
    }
}

package com.abdulmateen.pos_offline.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.LoadingSection
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.data.database.entities.OrderEntity
import com.abdulmateen.pos_offline.data.database.dao.CategoryRevenue
import com.abdulmateen.pos_offline.data.database.dao.TopProduct
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot() {
    val viewModel = koinViewModel<DashboardViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    DashboardScreen(uiState = uiState)
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            LoadingSection(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SummaryCards(uiState, deviceConfiguration)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TopProductsChart(
                            modifier = Modifier.weight(1f),
                            topProducts = uiState.topSellingProducts
                        )
                        if (deviceConfiguration != DeviceConfiguration.MOBILE_PORTRAIT) {
                            CategoryRevenueChart(
                                modifier = Modifier.weight(1f),
                                categoryRevenue = uiState.revenueByCategory
                            )
                        }
                    }
                }

                if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                    item {
                        CategoryRevenueChart(
                            modifier = Modifier.fillMaxWidth(),
                            categoryRevenue = uiState.revenueByCategory
                        )
                    }
                }

                item {
                    RecentOrdersSection(uiState.recentOrders)
                }
            }
        }
    }
}

@Composable
fun SummaryCards(uiState: DashboardUiState, deviceConfiguration: DeviceConfiguration) {
    val columns = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 2
        else -> 4
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val rows = (4 + columns - 1) / columns
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(columns) { colIndex ->
                    val index = rowIndex * columns + colIndex
                    if (index < 4) {
                        val (title, value, color) = when (index) {
                            0 -> Triple("Total Revenue", "$${uiState.totalRevenue}", MaterialTheme.colorScheme.primary)
                            1 -> Triple("Total Orders", "${uiState.totalOrders}", MaterialTheme.colorScheme.secondary)
                            2 -> Triple("Products", "${uiState.totalProducts}", MaterialTheme.colorScheme.tertiary)
                            else -> Triple("Total Stock", "${uiState.totalStock}", MaterialTheme.colorScheme.error)
                        }
                        SummaryCard(
                            modifier = Modifier.weight(1f),
                            title = title,
                            value = value,
                            containerColor = color
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    containerColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = containerColor)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = containerColor
            )
        }
    }
}

@Composable
fun TopProductsChart(
    modifier: Modifier = Modifier,
    topProducts: List<TopProduct>
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Top Products", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (topProducts.isEmpty()) {
                Text("No data available", style = MaterialTheme.typography.bodySmall)
            } else {
                topProducts.forEach { product ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = product.productName, style = MaterialTheme.typography.bodySmall)
                            Text(text = "${product.totalQty}", style = MaterialTheme.typography.bodySmall)
                        }
                        val maxQty = topProducts.maxOfOrNull { it.totalQty } ?: 1.0
                        Box(
                            modifier = Modifier
                                .fillMaxWidth( (product.totalQty / maxQty).toFloat().coerceIn(0.1f, 1f) )
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryRevenueChart(
    modifier: Modifier = Modifier,
    categoryRevenue: List<CategoryRevenue>
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Revenue by Category", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (categoryRevenue.isEmpty()) {
                Text("No data available", style = MaterialTheme.typography.bodySmall)
            } else {
                categoryRevenue.forEach { category ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = category.categoryName, style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "$${category.revenue}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentOrdersSection(orders: List<OrderEntity>) {
    Text(text = "Recent Orders", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (orders.isEmpty()) {
                Text("No recent orders", style = MaterialTheme.typography.bodySmall)
            } else {
                orders.forEach { order ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Order #${order.orderId}", fontWeight = FontWeight.SemiBold)
                            Text(text = order.paymentMethod, style = MaterialTheme.typography.labelSmall)
                        }
                        Text(
                            text = "$${order.total}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    POSOfflineTheme(
        content = {
            DashboardScreen(
                uiState = DashboardUiState(
                    totalRevenue = 15250.50,
                    totalOrders = 124,
                    totalProducts = 45,
                    totalStock = 1200.0,
                    topSellingProducts = listOf(
                        TopProduct("Apple", 50.0),
                        TopProduct("Banana", 30.0),
                        TopProduct("Cherry", 20.0)
                    ),
                    revenueByCategory = listOf(
                        CategoryRevenue("Fruits", 5000.0),
                        CategoryRevenue("Vegetables", 3000.0),
                        CategoryRevenue("Bakery", 2000.0)
                    ),
                    recentOrders = listOf(
                        OrderEntity(
                            orderId = 1,
                            customerName = "John Doe",
                            subTotal = 100.0,
                            discount = 10.0,
                            tax = 5.0,
                            total = 95.0,
                            paymentMethod = "Cash",
                            paymentStatus = "Paid"
                        ),
                        OrderEntity(
                            orderId = 2,
                            customerName = "Jane Smith",
                            subTotal = 200.0,
                            discount = 20.0,
                            tax = 10.0,
                            total = 190.0,
                            paymentMethod = "Card",
                            paymentStatus = "Paid"
                        )
                    ),
                    isLoading = false
                )
            )
        }
    )
}

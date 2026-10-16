package com.abdulmateen.pos_offline.feature.home.presentation.order


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.CartBadgeBox
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.LoadingSection
import pos_offline.composeapp.generated.resources.Res
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product
import androidx.compose.foundation.layout.height
import com.abdulmateen.pos_offline.feature.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.feature.home.presentation.components.CategoryFilterSection
import com.abdulmateen.pos_offline.feature.home.presentation.components.ProductListSection
import com.abdulmateen.pos_offline.feature.home.presentation.components.ProductSearchSection
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pos_offline.composeapp.generated.resources.app_name

@Composable
fun OrderScreenRoot(
    orderId: Long? = null,
    navigateToCart: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val viewModel: OrderViewModel = koinViewModel { parametersOf(orderId) }

    OrderScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToCart = navigateToCart,
        uiAction = viewModel::uiAction,
        toggleDarkTheme = toggleDarkTheme,
        isDarkTheme = isDarkTheme,
        searchField = viewModel.searchProductField.collectAsStateWithLifecycle().value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    searchField: String,
    uiState: OrderUiState,
    navigateToCart: () -> Unit,
    uiAction: (OrderUiAction) -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val productList = uiState.productList

    if (uiState.errorMessage != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { uiAction(OrderUiAction.ClearErrorMessage) },
            title = {
                Text(
                    text = "Stock Limit Reached",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = uiState.errorMessage.asString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { uiAction(OrderUiAction.ClearErrorMessage) }
                ) {
                    Text("OK")
                }
            }
        )
    }
    
    val isSmallScreen = when(deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT,
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_PORTRAIT -> true
        else -> false
    }

    if (isSmallScreen) {
        OrderScreenContent(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            searchField = searchField,
            uiState = uiState,
            uiAction = uiAction,
            deviceConfiguration = deviceConfiguration,
            productList = productList
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBarOrder(
                    toggleDarkTheme = toggleDarkTheme,
                    isDarkTheme = isDarkTheme,
                    deviceConfiguration = deviceConfiguration,
                    navigateToCart = navigateToCart,
                    cartItemsCount = uiState.cartItemCount
                )
            },
            contentWindowInsets = WindowInsets.navigationBars
        ) { innerPadding ->
            OrderScreenContent(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                searchField = searchField,
                uiState = uiState,
                uiAction = uiAction,
                deviceConfiguration = deviceConfiguration,
                productList = productList
            )
        }
    }
}

@Composable
private fun OrderScreenContent(
    modifier: Modifier,
    searchField: String,
    uiState: OrderUiState,
    uiAction: (OrderUiAction) -> Unit,
    deviceConfiguration: DeviceConfiguration,
    productList: List<Product>
) {
    Column(modifier = modifier) {
        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT,
            DeviceConfiguration.MOBILE_LANDSCAPE,
            DeviceConfiguration.TABLET_PORTRAIT -> {
                ProductCatalogSection(
                    modifier = Modifier.fillMaxSize(),
                    searchField = searchField,
                    uiState = uiState,
                    uiAction = uiAction,
                    productList = productList
                )
            }

            DeviceConfiguration.TABLET_LANDSCAPE -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProductCatalogSection(
                        modifier = Modifier.weight(1.2f),
                        searchField = searchField,
                        uiState = uiState,
                        uiAction = uiAction,
                        productList = productList
                    )
                    CartSummarySection(
                        modifier = Modifier.weight(0.8f),
                        uiState = uiState,
                        uiAction = uiAction
                    )
                }
            }

            DeviceConfiguration.DESKTOP -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProductCatalogSection(
                        modifier = Modifier.weight(1.3f),
                        searchField = searchField,
                        uiState = uiState,
                        uiAction = uiAction,
                        productList = productList
                    )
                    CartSummarySection(
                        modifier = Modifier.weight(0.7f),
                        uiState = uiState,
                        uiAction = uiAction
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCatalogSection(
    modifier: Modifier = Modifier,
    searchField: String,
    uiState: OrderUiState,
    uiAction: (OrderUiAction) -> Unit,
    productList: List<Product>
) {
    Column(modifier = modifier) {
        ProductSearchSection(
            modifier = Modifier.fillMaxWidth(),
            value = searchField,
            onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) },
            onBarcodeScanClick = { uiAction(OrderUiAction.OnBarcodeScanClick) },
            onCustomAmountClick = { uiAction(OrderUiAction.OnCustomAmountClick) },
            onHeldSalesClick = { uiAction(OrderUiAction.OnHeldSalesClick) },
            heldSalesCount = uiState.heldSalesCount
        )
        Spacer(modifier = Modifier.height(12.dp))
        CategoryFilterSection(
            modifier = Modifier.fillMaxWidth(),
            categoryList = uiState.categoryList,
            selectedCategoryId = uiState.selectedCategoryId,
            onCategorySelected = { uiAction(OrderUiAction.SelectCategory(it)) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.isLoading) {
            LoadingSection(modifier = Modifier.fillMaxWidth().weight(1f))
        } else {
            ProductListSection(
                modifier = Modifier.fillMaxWidth().weight(1f),
                list = productList,
                currencySymbol = uiState.currencySymbol,
                onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarOrder(
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean,
    deviceConfiguration: DeviceConfiguration,
    navigateToCart: () -> Unit,
    cartItemsCount: Int
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            LogoImage(modifier = Modifier.size(48.dp))
        },
        title = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.labelLarge,
                color = Blue
            )
        },
        actions = {
            Row {
                if (deviceConfiguration != DeviceConfiguration.DESKTOP){
                    CartBadgeBox(itemCount = cartItemsCount, onCartClick = navigateToCart)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if(isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                        .clickable(
                            onClick = toggleDarkTheme
                        )
                )
            }
        }
    )
}

@Preview
@Composable
fun ProductListScreenPreview() {
    val uiState = OrderUiState(
        productList = listOf(
            Product(
                productId = 123,
                name = "Product Name",
                sku = "sku123",
                price = 1200.0,
                quantity = 1200.0,
                photoBytes = byteArrayOf(),

            ),
            Product(
                productId = 123,
                name = "Product Name",
                sku = "sku456",
                price = 1200.0,
                quantity = 1200.0,
                photoBytes = byteArrayOf()
            )
        )
    )
    POSOfflineTheme(
        darkTheme = false,
        content = {
            OrderScreen(
                uiState = uiState,
                navigateToCart = {},
                uiAction = {},
                toggleDarkTheme = {},
                isDarkTheme = false,
                searchField = ""
            )
        }
    )

}
@Preview(name = "CreateOrderScreenPreview Dark")
@Composable
fun CreateOrderScreenPreviewDark() {
    val uiState = OrderUiState(
        productList = listOf(
            Product(
                productId = 123,
                name = "Product Name",
                sku = "as12",
                price = 1200.0,
                quantity = 1200.0,
                unit = ItemUnit(
                    unitId = 1,
                    name = "Kilogram",
                    symbol = "kg"
                )
            ),
            Product(
                productId = 123,
                name = "Product Name",
                sku = "as12",
                price = 1200.0,
                quantity = 1200.0,
                unit = ItemUnit(
                    unitId = 1,
                    name = "Kilogram",
                    symbol = "kg"
                )
            )
        )
    )
    POSOfflineTheme(
        darkTheme = true,
        content = {
            OrderScreen(
                uiState = uiState,
                navigateToCart = {},
                uiAction = {},
                toggleDarkTheme = {},
                isDarkTheme = false,
                searchField = ""
            )
        }
    )
}

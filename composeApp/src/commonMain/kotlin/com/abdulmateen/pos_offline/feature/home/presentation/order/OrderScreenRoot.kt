package com.abdulmateen.pos_offline.feature.home.presentation.order


import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.abdulmateen.pos_offline.core.designsystem.components.CartBadgeBox
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
import com.abdulmateen.pos_offline.core.designsystem.components.layouts.LoadingSection
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.tally_trades_logo
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.feature.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.feature.home.presentation.components.ProductListSection
import com.abdulmateen.pos_offline.feature.home.presentation.components.ProductSearchSection
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.app_name

@Composable
fun OrderScreenRoot(
    navigateToCart: () -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val viewModel: OrderViewModel = koinViewModel()
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

    Scaffold(
        topBar = {
            if (deviceConfiguration != DeviceConfiguration.MOBILE_LANDSCAPE) {
                TopAppBarOrder(
                    toggleDarkTheme = toggleDarkTheme,
                    isDarkTheme = isDarkTheme,
                    deviceConfiguration = deviceConfiguration,
                    navigateToCart = navigateToCart,
                    cartItemsCount = uiState.cartItemCount
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) {innerPadding ->
        val rootModifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 16.dp)

        when(deviceConfiguration){
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                Column(
                    modifier = rootModifier
                ) {
                    ProductSearchSection(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchField,
                        onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    if (uiState.isLoading){
                        LoadingSection(modifier = Modifier.fillMaxWidth().weight(.1f))
                    }else{
                        ProductListSection(
                            modifier = Modifier.fillMaxWidth(),
                            list = productList,
                            currencySymbol = uiState.currencySymbol,
                            onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
                        )
                    }
                }
            }
            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchField,
                            onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        if (uiState.isLoading){
                            LoadingSection(modifier = Modifier.fillMaxWidth().weight(.1f))
                        }else {
                            ProductListSection(
                                modifier = Modifier.fillMaxWidth(),
                                list = productList,
                                currencySymbol = uiState.currencySymbol,
                                onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
                            )
                        }
                    }
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT -> {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchField,
                            onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) }
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        if (uiState.isLoading) {
                            LoadingSection(modifier = Modifier.fillMaxWidth().weight(.1f))
                        } else{
                            ProductListSection(
                                modifier = Modifier.fillMaxWidth(),
                                list = productList,
                                currencySymbol = uiState.currencySymbol,
                                onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
                            )
                    }
                    }
            }
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchField,
                            onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        if (uiState.isLoading){
                            LoadingSection(modifier = Modifier.fillMaxWidth().weight(.1f))
                        }else {
                            ProductListSection(
                                modifier = Modifier.fillMaxWidth(),
                                list = productList,
                                currencySymbol = uiState.currencySymbol,
                                onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
                            )
                        }
                    }
                    CartSummarySection(
                        modifier = Modifier.weight(.1f),
                        uiState = uiState,
                        uiAction = uiAction
                    )
                }
            }
            DeviceConfiguration.DESKTOP -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ){
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth(),
                            value = searchField,
                            onValueChange = { uiAction(OrderUiAction.OnSearchProduct(it)) }
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        if (uiState.isLoading){
                            LoadingSection(modifier = Modifier.fillMaxWidth().weight(.1f))
                        }else {
                            ProductListSection(
                                modifier = Modifier.fillMaxWidth(),
                                list = productList,
                                currencySymbol = uiState.currencySymbol,
                                onAddToCart = { uiAction(OrderUiAction.AddProductToCart(it)) }
                            )
                        }
                    }
                    CartSummarySection(
                        modifier = Modifier.weight(.1f),
                        uiState = uiState,
                        uiAction = uiAction
                    )
                }
            }
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
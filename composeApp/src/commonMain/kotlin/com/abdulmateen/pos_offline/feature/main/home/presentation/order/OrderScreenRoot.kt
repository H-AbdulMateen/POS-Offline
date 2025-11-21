package com.abdulmateen.pos_offline.feature.main.home.presentation.order


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
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.compose_multiplatform
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.home.domain.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.Rating
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.CartSummarySection
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.CustomerSection
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.ProductListSection
import com.abdulmateen.pos_offline.feature.main.home.presentation.components.ProductSearchSection
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
    val viewModel: ProductListViewModel = koinViewModel()
    OrderScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToCart = navigateToCart,
        uiAction = viewModel::uiAction,
        toggleDarkTheme = toggleDarkTheme,
        isDarkTheme = isDarkTheme
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    uiState: ProductListUiState,
    navigateToCart: () -> Unit,
    uiAction: (ProductListUiAction) -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    Scaffold(
        topBar = {
            if (deviceConfiguration != DeviceConfiguration.MOBILE_LANDSCAPE) {
                TopAppBarOrder(
                    toggleDarkTheme = toggleDarkTheme,
                    isDarkTheme = isDarkTheme,
                    deviceConfiguration = deviceConfiguration,
                    navigateToCart = navigateToCart
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
                    CustomerSection(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    ProductSearchSection(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.size(8.dp))
                    ProductListSection(modifier = Modifier.fillMaxWidth())
                }
            }
            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomerSection(
                        modifier = Modifier.weight(.1f)
                    )
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        ProductListSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    CustomerSection(
                        modifier = Modifier.weight(.1f)
                    )
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        ProductListSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            DeviceConfiguration.TABLET_LANDSCAPE -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    CustomerSection(
                        modifier = Modifier.weight(.1f)
                    )
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        ProductListSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CartSummarySection(
                        modifier = Modifier.weight(.1f)
                    )
                }
            }
            DeviceConfiguration.DESKTOP -> {
                Row(
                    modifier = rootModifier,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ){
                    CustomerSection(
                        modifier = Modifier.weight(.1f)
                    )
                    Column(
                        modifier = Modifier.weight(.1f)
                    ) {
                        ProductSearchSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        ProductListSection(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CartSummarySection(
                        modifier = Modifier.weight(.1f)
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
    navigateToCart: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
                Image(
                    painter = painterResource(resource = Res.drawable.compose_multiplatform),
                    contentDescription = "LogoImage",
                    modifier = Modifier.size(48.dp)
                )
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
                    CartBadgeBox(itemCount = 5, onCartClick = navigateToCart)
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
    val uiState = ProductListUiState(
        productList = listOf(
            Product(
                id = 1,
                title = "Product 1",
                price = 10.0,
                description = "Description 1",
                category = "Category 1",
                image = "",
                rating = Rating(
                    rate = 4.5,
                    count = 100
                )
            ),
            Product(
                id = 2,
                title = "Product 2",
                price = 20.0,
                description = "Description 2",
                category = "Category 2",
                image = "",
                rating = Rating(rate = 4.0, count = 200)
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
                isDarkTheme = false
            )
        }
    )

}
@Preview(name = "CreateOrderScreenPreview Dark")
@Composable
fun CreateOrderScreenPreviewDark() {
    val uiState = ProductListUiState(
        productList = listOf(
            Product(
                id = 1,
                title = "Product 1",
                price = 10.0,
                description = "Description 1",
                category = "Category 1",
                image = "",
                rating = Rating(
                    rate = 4.5,
                    count = 100
                )
            ),
            Product(
                id = 2,
                title = "Product 2",
                price = 20.0,
                description = "Description 2",
                category = "Category 2",
                image = "",
                rating = Rating(rate = 4.0, count = 200)
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
                isDarkTheme = false
            )
        }
    )
}
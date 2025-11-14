package com.abdulmateen.pos_offline.feature.main.home.presentation.product_list


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.compose_multiplatform
import pos_offline.composeapp.generated.resources.you_are_viewing_offline_data
import com.abdulmateen.pos_offline.core.designsystem.components.ErrorSurface
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.main.home.domain.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.Rating
import com.abdulmateen.pos_offline.feature.main.home.presentation.product_list.components.ProductListItem
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.clear_day
import pos_offline.composeapp.generated.resources.nightlight

@Composable
fun ProductListScreenRoot(
    navigateToProductDetail: (Int) -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val viewModel: ProductListViewModel = koinViewModel()
    ProductListScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToProductDetail = navigateToProductDetail,
        uiAction = viewModel::uiAction,
        toggleDarkTheme = toggleDarkTheme,
        isDarkTheme = isDarkTheme
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    uiState: ProductListUiState,
    navigateToProductDetail: (Int) -> Unit,
    uiAction: (ProductListUiAction) -> Unit,
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
//    var isRefreshing by remember { mutableStateOf(false) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val columnSize = when(deviceConfiguration){
        DeviceConfiguration.MOBILE_PORTRAIT -> 2
        DeviceConfiguration.MOBILE_LANDSCAPE -> 3
        DeviceConfiguration.TABLET_PORTRAIT -> 3
        DeviceConfiguration.TABLET_LANDSCAPE -> 4
        DeviceConfiguration.DESKTOP -> 5
    }
    Scaffold(
        topBar = {
            TopBarCentered(
                toggleDarkTheme = toggleDarkTheme,
                isDarkTheme = isDarkTheme
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) {innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize()
                .consumeWindowInsets(WindowInsets.statusBars)
                .padding(innerPadding)
            ,
            state = pullToRefreshState,
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    uiAction(ProductListUiAction.ForceReload)
                }
            },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.errorMessage != null && uiState.productList.isEmpty()) {
                Text(
                    text = uiState.errorMessage.asString(),
                    style = MaterialTheme.typography.labelMedium
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.errorMessage != null) {
                        ErrorSurface(
                            modifier = Modifier.fillMaxWidth(),
                            errorMessage = stringResource(Res.string.you_are_viewing_offline_data)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        columns = GridCells.Fixed(count = columnSize),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(items = uiState.productList) { item ->
                            ProductListItem(
                                onClick = {
                                    navigateToProductDetail(item.id)
                                },
                                item = item,
                                uiAction = uiAction
                            )
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarCentered(
    toggleDarkTheme: () -> Unit,
    isDarkTheme: Boolean
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
                text = "Welcome",
                style = MaterialTheme.typography.labelLarge,
                color = Blue
            )
        },
        actions = {
            Row {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(if(isDarkTheme) Res.drawable.clear_day else Res.drawable.nightlight),
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
    ProductListScreen(
        uiState = uiState,
        navigateToProductDetail = {},
        uiAction = {},
        toggleDarkTheme = {},
        isDarkTheme = false
    )
}
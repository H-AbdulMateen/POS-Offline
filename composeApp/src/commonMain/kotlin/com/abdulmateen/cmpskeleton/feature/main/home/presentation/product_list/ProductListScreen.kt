package com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list


import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmpskeleton.composeapp.generated.resources.Res
import cmpskeleton.composeapp.generated.resources.compose_multiplatform
import cmpskeleton.composeapp.generated.resources.you_are_viewing_offline_data
import com.abdulmateen.cmpskeleton.core.presentation.components.ErrorSurface
import com.abdulmateen.cmpskeleton.core.utils.DeviceConfiguration
import com.abdulmateen.cmpskeleton.feature.main.home.domain.Product
import com.abdulmateen.cmpskeleton.feature.main.home.domain.Rating
import com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_list.components.ProductListItem
import com.abdulmateen.cmpskeleton.ui.theme.Surface
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductListScreenRoot(
    navigateToProductDetail: (Int) -> Unit
) {
    val viewModel: ProductListViewModel = koinViewModel()
    ProductListScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        navigateToProductDetail = navigateToProductDetail,
        uiAction = viewModel::uiAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    uiState: ProductListUiState,
    navigateToProductDetail: (Int) -> Unit,
    uiAction: (ProductListUiAction) -> Unit
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
            TopBarCentered()
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
private fun TopBarCentered() {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {

    }
    CenterAlignedTopAppBar(
        navigationIcon = {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Welcome",
                style = MaterialTheme.typography.labelLarge,
                color = Blue
            )
        },
        title = {
            Image(
                painter = painterResource(resource = Res.drawable.compose_multiplatform),
                contentDescription = "LogoImage",
                modifier = Modifier.size(48.dp)
            )
        },
        actions = {
            Row {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(36.dp)
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
        uiAction = {}
    )
}
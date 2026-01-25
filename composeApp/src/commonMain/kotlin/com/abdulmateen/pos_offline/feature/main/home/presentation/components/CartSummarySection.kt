package com.abdulmateen.pos_offline.feature.main.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.domain.models.CartItem
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.OrderUiAction
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.OrderUiState
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.cart_summary

@Composable
fun CartSummarySection(
    modifier: Modifier = Modifier,
    uiState: OrderUiState,
    uiAction: (OrderUiAction) -> Unit
) {
    var showCheckoutDialog by rememberSaveable { mutableStateOf(false) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        val cartItemList = uiState.cartItems
        when(deviceConfiguration){
            DeviceConfiguration.MOBILE_LANDSCAPE,
            DeviceConfiguration.TABLET_LANDSCAPE-> {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(.1f)
                    ) {
                        items(items = cartItemList) {cartItem ->
                            CartListItem(
                                item = cartItem,
                                uiAction = uiAction
                            )
                        }
                    }
                    VerticalDivider(
                        Modifier.padding(vertical = 8.dp),
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
                    TotalCheckoutSection(
                        onProceedToCheckout = {
                            showCheckoutDialog = true
                        },
                        modifier = Modifier.fillMaxHeight(),
                        totalAmount = uiState.subTotal
                    )
                }
            }else -> {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = stringResource(Res.string.cart_summary),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(.1f)
                ) {
                    items(items = cartItemList) {
                        CartListItem(
                            item = it,
                            uiAction = uiAction
                        )
                    }
                }
                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
                TotalCheckoutSection(
                    onProceedToCheckout = {
                        showCheckoutDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    totalAmount = uiState.subTotal
                )
            }
            }
        }
    }
    if (showCheckoutDialog) {
        PaymentDialog(
            onDismiss = { showCheckoutDialog = false },
            onConfirm = {
                showCheckoutDialog = false
            },
            totalAmount = 1000.0
        )
    }
}

@Preview(name = "Cart Light Mode")
@Composable
fun CartSummarySectionPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CartSummarySection(
                uiState = OrderUiState(),
                uiAction = {}
            )
        }
    )
}

@Preview(name = "Cart Dark Mode")
@Composable
fun CartSummarySectionPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CartSummarySection(
                uiState = OrderUiState(),
                uiAction = {}
            )
        }
    )
}

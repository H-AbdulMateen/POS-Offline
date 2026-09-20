package com.abdulmateen.pos_offline.feature.home.presentation.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.buttons.MyButtonPrimary
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.abdulmateen.pos_offline.core.designsystem.components.dialogs.SingleTextFieldDialog
import com.abdulmateen.pos_offline.core.utils.DeviceConfiguration
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderUiAction
import com.abdulmateen.pos_offline.feature.home.presentation.order.OrderUiState
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.are_you_sure
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.cart_summary
import pos_offline.composeapp.generated.resources.delete
import pos_offline.composeapp.generated.resources.discount
import pos_offline.composeapp.generated.resources.proceed_to_checkout
import pos_offline.composeapp.generated.resources.tax

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
                                isCustomizablePriceEnabled = uiState.isCustomizablePriceEnabled,
                                currencySymbol = uiState.currencySymbol,
                                uiAction = uiAction
                            )
                        }
                    }
                    VerticalDivider(
                        Modifier.padding(vertical = 8.dp),
                        DividerDefaults.Thickness,
                        DividerDefaults.color
                    )
                    Column(
                        modifier = Modifier.fillMaxHeight().weight(.1f)
                    ) {

                        TotalCheckoutSection(
                            modifier = Modifier,
                            subTotal = uiState.subTotal,
                            uiAction = uiAction,
                            uiState = uiState
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MyButtonPrimary(
                            onClick = {
                                showCheckoutDialog = true
                            },
                            text = stringResource(Res.string.proceed_to_checkout),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }else -> {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(.1f)
                ) {
                    items(items = cartItemList) {
                        CartListItem(
                            item = it,
                            isCustomizablePriceEnabled = uiState.isCustomizablePriceEnabled,
                            currencySymbol = uiState.currencySymbol,
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
                    modifier = Modifier.fillMaxWidth(),
                    subTotal = uiState.subTotal,
                    uiAction = uiAction,
                    uiState = uiState
                )

                Spacer(modifier = Modifier.height(16.dp))
                MyButtonPrimary(
                    onClick = {
                        showCheckoutDialog = true
                    },
                    text = stringResource(Res.string.proceed_to_checkout),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            }
        }
    }
    if (showCheckoutDialog) {
        PaymentDialog(
            cartItems = uiState.cartItems,
            subTotal = uiState.subTotal,
            discount = uiState.discount,
            tax = uiState.tax,
            totalAmount = uiState.total,
            businessName = uiState.businessName,
            currencySymbol = uiState.currencySymbol,
            customerList = uiState.customerList,
            selectedCustomer = uiState.selectedCustomer,
            onSelectCustomer = { uiAction(OrderUiAction.SelectCustomer(it)) },
            onDismiss = { showCheckoutDialog = false },
            onConfirm = { name, phone ->
                showCheckoutDialog = false
                uiAction(OrderUiAction.Checkout(name, phone))
            },
            onConfirmCredit = { name, phone, paid ->
                showCheckoutDialog = false
                uiAction(OrderUiAction.CreateCredit(name, phone, paid))
            }
        )
    }
    if (uiState.isDeleteDialogVisible) {
        DestructiveConfirmationDialog(
            onDismiss = { uiAction(OrderUiAction.ToggleDeleteDialog()) },
            title = stringResource(Res.string.delete),
            description = stringResource(Res.string.are_you_sure),
            confirmButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = { uiAction(OrderUiAction.RemoveCartItem) },
            onCancelClick = { uiAction(OrderUiAction.ToggleDeleteDialog()) },
        )
    }
    if (uiState.isDiscountDialogVisible){
        SingleTextFieldDialog(
            title = stringResource(Res.string.discount),
            onConfirm = { uiAction(OrderUiAction.ApplyDiscount) },
            onDismiss = { uiAction(OrderUiAction.ToggleDiscountDialog) },
            hint = stringResource(Res.string.discount),
            textFieldValue = uiState.discountField,
            keyboardType = KeyboardType.Decimal,
            textFieldError = if(uiState.discountFieldErrorMessage != null)  stringResource(uiState.discountFieldErrorMessage) else "",
            hasError = uiState.hasDiscountError,
            onValueChange = {uiAction(OrderUiAction.UpdateDiscountField(it))}
        )
    }
    if (uiState.isTaxDialogVisible){
        SingleTextFieldDialog(
            title = stringResource(Res.string.tax),
            onConfirm = { uiAction(OrderUiAction.ApplyTax) },
            onDismiss = { uiAction(OrderUiAction.ToggleTaxDialog) },
            hint = stringResource(Res.string.tax),
            textFieldValue = uiState.taxField,
            keyboardType = KeyboardType.Decimal,
            textFieldError = if(uiState.taxFieldErrorMessage != null)  stringResource(uiState.taxFieldErrorMessage) else "",
            hasError = uiState.hasTaxError,
            onValueChange = {uiAction(OrderUiAction.UpdateTaxField(it))}
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

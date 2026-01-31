package com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abdulmateen.pos_offline.common.presentation.components.ProductPhoto
import com.abdulmateen.pos_offline.core.designsystem.components.AnimatedErrorText
import com.abdulmateen.pos_offline.core.designsystem.components.LocalImageWidget
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.core.designsystem.components.WheelDateTimePickerDialog
import com.abdulmateen.pos_offline.core.utils.formatDatePlatform
import com.abdulmateen.pos_offline.domain.models.Category
import com.abdulmateen.pos_offline.domain.models.Product
import com.abdulmateen.pos_offline.domain.models.ItemUnit
import com.abdulmateen.pos_offline.domain.models.ProductDetail
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryUiState
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.models.ProductUi
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.toByteArray
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add
import pos_offline.composeapp.generated.resources.add_new_product
import pos_offline.composeapp.generated.resources.barcode_reader
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.edit_product
import pos_offline.composeapp.generated.resources.product_name
import pos_offline.composeapp.generated.resources.purchase_price
import pos_offline.composeapp.generated.resources.quantity_in_stock
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.scan_barcode
import pos_offline.composeapp.generated.resources.select_category
import pos_offline.composeapp.generated.resources.select_item_unit
import pos_offline.composeapp.generated.resources.sku
import pos_offline.composeapp.generated.resources.update
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun AddEditInventoryDialog(
    uiState: InventoryUiState,
    uiAction: (InventoryUiAction) -> Unit,
    item: ProductDetail?,                   // null = Add, not-null = Edit
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val isEditing = item != null

    var datePickerDialog by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var unitMenuExpanded by remember { mutableStateOf(false) }



    val imageCropper = rememberImageCropper()
    var openImagePicker by remember { mutableStateOf(value = false) }

        CMPImagePickNCropDialog(
            imageCropper = imageCropper,
            openImagePicker = openImagePicker,
            imagePickerDialogHandler = {
                openImagePicker = it
            },
            selectedImageCallback = {
                uiAction(
                    InventoryUiAction.OnImageSelection(
                        imageBitmap = it,
                        bytes = it.toByteArray(format = ImageFileFormat.PNG, quality = 1.0f)
                    )
                )
            },
            selectedImageFileCallback = {}
        )



    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier =  Modifier.padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                Text(
                    text = if (isEditing) stringResource(Res.string.edit_product) else stringResource(Res.string.add_new_product),
                    style = MaterialTheme.typography.headlineSmall
                )

                //User Profile Image
                if (isEditing && uiState.imageBitmap != null){
                    ProductPhoto(
                        photoBytes = item.photoBytes,
                        contentDescription = item.name,
                        modifier = Modifier.clickable(
                            onClick = {
                                openImagePicker = true
                            }
                        )
                    )
                }else {
                    LocalImageWidget(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                            .clickable { openImagePicker = true }
                            .background(LightGray.takeIf { uiState.imageBitmap == null }
                                ?: Transparent),
                        selectedImage = uiState.imageBitmap
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTF(
                    value = item?.name ?: uiState.name,
                    onValueChange = { uiAction(InventoryUiAction.OnNameChange(it)) },
                    placeholder = stringResource(Res.string.product_name),
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasNameError,
                    errorMessage = uiState.nameErrorText
                )
                OutlinedTF(
                    value = item?.sku ?: uiState.sku,
                    onValueChange = { uiAction(InventoryUiAction.OnSkuChange(it)) },
                    placeholder = stringResource(Res.string.sku),
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasSkuError,
                    errorMessage = uiState.skuErrorText
                )

                OutlinedTF(
                    value = item?.barcode ?: uiState.barcode,
                    onValueChange = { uiAction(InventoryUiAction.OnBarcodeChange(it)) },
                    placeholder = stringResource(Res.string.scan_barcode),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(painter = painterResource(Res.drawable.barcode_reader), contentDescription = "Barcode Reader", modifier = Modifier.size(24.dp)) },
                    hasError = uiState.hasBarcodeError,
                    errorMessage = uiState.barcodeErrorText
                )


                OutlinedTF(
                    value = if (isEditing) item.stock.toString() else uiState.stock,
                    onValueChange = { uiAction(InventoryUiAction.OnStockChange(it)) },
                    keyboardType = KeyboardType.Decimal,
                    placeholder = stringResource(Res.string.quantity_in_stock),
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasStockError,
                    errorMessage = uiState.stockErrorText
                )

                OutlinedTF(
                    value = if (isEditing) item.price.toString() else uiState.salePrice,
                    onValueChange = { uiAction(InventoryUiAction.OnSalesPriceChange(it)) },
                    placeholder = stringResource(Res.string.sales_price),
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasSalesPriceError,
                    errorMessage = uiState.salesPriceErrorText
                )

                OutlinedTF(
                    value = if (isEditing) item.purchasePrice.toString() else uiState.purchasePrice,
                    onValueChange = { uiAction(InventoryUiAction.OnPurchasePriceChange(it)) },
                    placeholder = stringResource(Res.string.purchase_price),
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasPurchasePriceError,
                    errorMessage = uiState.purchasePriceErrorText
                )
//                OutlinedTFDate(
//                    value = uiState.itemExpiryDate,
//                    onClick = { datePickerDialog = true },
//                    modifier = Modifier.fillMaxWidth(),
//                    placeholder = stringResource(Res.string.expiry_date)
//                )

                ItemUnitRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedItemUnit = item?.unit ?: uiState.unit,
                    unitMenuExpanded = unitMenuExpanded,
                    toggleUnitMenu = { unitMenuExpanded = !unitMenuExpanded },
                    selectedUnitChange = { uiAction(InventoryUiAction.OnItemUnitChange(it)) },
                    addEditUnit = { uiAction(InventoryUiAction.ToggleUnitDialog) },
                    list = uiState.unitList
                )

                CategoryRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = item?.category ?: uiState.category,
                    categoryMenuExpanded = categoryMenuExpanded,
                    toggleCategoryMenu = { categoryMenuExpanded = !categoryMenuExpanded },
                    selectedCategoryChange = { uiAction(InventoryUiAction.OnCategoryChange(it)) },
                    addEditCategory = { uiAction(InventoryUiAction.ToggleCategoryDialog) },
                    list = uiState.categoryList
                )
//                SubCategoryRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    selectedCategory = selectedSubCategory,
//                    subCategoryMenuExpanded = subCategoryMenuExpanded,
//                    toggleCategoryMenu = { subCategoryMenuExpanded = !subCategoryMenuExpanded },
//                    selectedCategoryChange = { selectedSubCategory = it },
//                    addEditCategory = { subCategoryAddEditDialogPopup = true }
//                )
                AnimatedErrorText(
                    modifier = Modifier.fillMaxWidth(),
                    visible = uiState.errorResult != null,
                    errorMessage = uiState.errorResult?.asString()
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(stringResource(Res.string.cancel))
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = onSave
                    ) {
                        Text(if (isEditing) stringResource(Res.string.update) else stringResource(Res.string.add))
                    }
                }
            }
            if (uiState.unitDialogVisible){
                AddEditItemUnitDialog(
                    itemUnit = null,
                    onDismiss = { uiAction(InventoryUiAction.ToggleUnitDialog) },
                    onConfirm = { uiAction(InventoryUiAction.OnAddNewUnit) },
                    uiState = uiState,
                    uiAction = uiAction
                )
            }
            if (uiState.categoryDialogVisible){
                AddEditCategoryDialog(
                    category = null,
                    onDismiss = { uiAction(InventoryUiAction.ToggleCategoryDialog) },
                    onConfirm = { uiAction(InventoryUiAction.OnAddNewCategory) },
                    uiState = uiState,
                    uiAction = uiAction
                )
            }
        }
    }
    if (datePickerDialog) {
        WheelDateTimePickerDialog(
            showDatePicker = datePickerDialog,
            toggleDatePicker = {
                datePickerDialog = false
                               },
            onDateSelection = {
                uiAction(InventoryUiAction.OnExpiryDateChange(formatDatePlatform(it, "dd/MM/yyyy")))
                datePickerDialog = false
            }
        )
    }
}

@Composable
fun CategoryRow(
    modifier: Modifier,
    toggleCategoryMenu: () -> Unit = {},
    selectedCategory: Category? = null,
    categoryMenuExpanded: Boolean = false,
    selectedCategoryChange: (Category) -> Unit,
    addEditCategory: () -> Unit,
    list: List<Category>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Box(
            modifier = Modifier.weight(1f)
        ){
            Card(modifier = Modifier.fillMaxWidth()
                .clickable(
                    onClick = toggleCategoryMenu
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = selectedCategory?.name ?: stringResource(Res.string.select_category))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "ArrowDropdown")
                }
            }
            CategoryDropdown(
                categoryMenuExpanded = categoryMenuExpanded,
                categoryMenuExpandedChange = toggleCategoryMenu,
                selectedCategoryChange = {
                    selectedCategoryChange(it)
                    toggleCategoryMenu()
                },
                list = list
            )
        }
        IconButton(
            onClick = addEditCategory
        ){
            Icon(imageVector = Icons.Default.Add, contentDescription = "AddIcon")
        }
    }
}

@Composable
fun ItemUnitRow(
    modifier: Modifier,
    toggleUnitMenu: () -> Unit = {},
    selectedItemUnit: ItemUnit? = null,
    unitMenuExpanded: Boolean = false,
    selectedUnitChange: (ItemUnit) -> Unit,
    addEditUnit: () -> Unit,
    list: List<ItemUnit>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Box(
            modifier = Modifier.weight(1f)
        ){
            Card(modifier = Modifier.fillMaxWidth()
                .clickable(
                    onClick = toggleUnitMenu
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = selectedItemUnit?.name ?: stringResource(Res.string.select_item_unit))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "ArrowDropdown")
                }
            }
            ItemUnitDropdown(
                itemUnitMenuExpanded = unitMenuExpanded,
                unitMenuExpandedChange = toggleUnitMenu,
                selectedUnitChange = {
                    selectedUnitChange(it)
                    toggleUnitMenu()
                },
                unitList = list
            )
        }
        IconButton(
            onClick = addEditUnit
        ){
            Icon(imageVector = Icons.Default.Add, contentDescription = "AddIcon")
        }
    }
}


@Composable
fun ItemUnitDropdown(
    itemUnitMenuExpanded: Boolean,
    unitMenuExpandedChange: () -> Unit,
    selectedUnitChange: (ItemUnit) -> Unit,
    unitList: List<ItemUnit>
){
    DropdownMenu(
        expanded = itemUnitMenuExpanded,
        onDismissRequest = unitMenuExpandedChange,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        unitList.forEach { itemUnits ->
            DropdownMenuItem(
                text = { Text(text = itemUnits.name) },
                onClick = { selectedUnitChange(itemUnits) }
            )
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun AddEditInventoryDialogPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            AddEditInventoryDialog(
                item = null,
                onDismiss = {},
                onSave = {},
                uiAction = {},
                uiState = InventoryUiState()
            )
        }
    )
}

@Preview(name = "Dark Mode")
@Composable
fun AddEditInventoryDialogPreviewDark(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            AddEditInventoryDialog(
                item = null,
                onDismiss = {},
                onSave = {},
                uiAction = {},
                uiState = InventoryUiState()
            )
        }
    )
}
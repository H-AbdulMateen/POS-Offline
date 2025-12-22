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
import com.abdulmateen.pos_offline.core.designsystem.components.LocalImageWidget
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTFDate
import com.abdulmateen.pos_offline.core.designsystem.components.WheelDateTimePickerDialog
import com.abdulmateen.pos_offline.core.utils.formatDatePlatform
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.models.ItemUnit
import com.abdulmateen.pos_offline.feature.main.home.domain.models.SubCategory
import com.abdulmateen.pos_offline.feature.main.home.domain.models.dummySubCategories
import com.abdulmateen.pos_offline.feature.main.home.domain.models.dummyItemUnits
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryUiAction
import com.abdulmateen.pos_offline.feature.main.inventory.presentation.InventoryUiState
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
import pos_offline.composeapp.generated.resources.expiry_date
import pos_offline.composeapp.generated.resources.product_name
import pos_offline.composeapp.generated.resources.purchase_price
import pos_offline.composeapp.generated.resources.quantity_in_stock
import pos_offline.composeapp.generated.resources.sales_price
import pos_offline.composeapp.generated.resources.scan_barcode
import pos_offline.composeapp.generated.resources.select_category
import pos_offline.composeapp.generated.resources.select_item_unit
import pos_offline.composeapp.generated.resources.select_sub_category
import pos_offline.composeapp.generated.resources.sku
import pos_offline.composeapp.generated.resources.update
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun AddEditInventoryDialog(
    uiState: InventoryUiState,
    uiAction: (InventoryUiAction) -> Unit,
    item: Product?,                   // null = Add, not-null = Edit
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    val isEditing = item != null

    var datePickerDialog by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var subCategoryMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null)}
    var selectedSubCategory by remember { mutableStateOf<SubCategory?>(null)}
    var categoryAddEditDialogPopup by remember { mutableStateOf(false) }
    var subCategoryAddEditDialogPopup by remember { mutableStateOf(false) }
    var selectedItemUnit by remember { mutableStateOf<ItemUnit?>(null) }
    var unitAddEditDialogPopup by remember { mutableStateOf(false) }
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
            uiAction(InventoryUiAction.OnImageUrlChange(it.toByteArray(format = ImageFileFormat.PNG, quality = 1.0f)))
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
                LocalImageWidget(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .clickable { openImagePicker = true }
                        .background(LightGray.takeIf { uiState.imageBitmap == null } ?: Transparent),
                    selectedImage = uiState.imageBitmap
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTF(
                    value = uiState.name,
                    onValueChange = { uiAction(InventoryUiAction.OnNameChange(it)) },
                    placeholder = stringResource(Res.string.product_name),
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasNameError,
                    errorMessage = uiState.nameErrorText
                )

                OutlinedTF(
                    value = uiState.sku,
                    onValueChange = { uiAction(InventoryUiAction.OnSkuChange(it)) },
                    placeholder = stringResource(Res.string.scan_barcode),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Icon(painter = painterResource(Res.drawable.barcode_reader), contentDescription = "Barcode Reader", modifier = Modifier.size(24.dp)) },
                    hasError = uiState.hasSkuError,
                    errorMessage = uiState.skuErrorText
                )


                OutlinedTF(
                    value = uiState.quantity,
                    onValueChange = { 
                        if (it.all { c -> c.isDigit() }) { uiAction(InventoryUiAction.OnQuantityChange(it)) }
                    },
                    keyboardType = KeyboardType.Decimal,
                    placeholder = stringResource(Res.string.quantity_in_stock),
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasQuantityError,
                    errorMessage = uiState.quantityErrorText
                )

                OutlinedTF(
                    value = uiState.salesPrice,
                    onValueChange = { uiAction(InventoryUiAction.OnSalesPriceChange(it)) },
                    placeholder = stringResource(Res.string.sales_price),
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasSalesPriceError,
                    errorMessage = uiState.salesPriceErrorText
                )

                OutlinedTF(
                    value = uiState.purchasePrice,
                    onValueChange = { uiAction(InventoryUiAction.OnPurchasePriceChange(it)) },
                    placeholder = stringResource(Res.string.purchase_price),
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.fillMaxWidth(),
                    hasError = uiState.hasPurchasePriceError,
                    errorMessage = uiState.purchasePriceErrorText
                )
                OutlinedTFDate(
                    value = uiState.itemExpiryDate,
                    onClick = { datePickerDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(Res.string.expiry_date)
                )

                ItemUnitRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedItemUnit = selectedItemUnit,
                    unitMenuExpanded = unitMenuExpanded,
                    toggleUnitMenu = { unitMenuExpanded = !unitMenuExpanded },
                    selectedUnitChange = { selectedItemUnit = it },
                    addEditUnit = { unitAddEditDialogPopup = true }
                )

                CategoryRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = selectedCategory,
                    categoryMenuExpanded = categoryMenuExpanded,
                    toggleCategoryMenu = { categoryMenuExpanded = !categoryMenuExpanded },
                    selectedCategoryChange = { selectedCategory = it },
                    addEditCategory = { categoryAddEditDialogPopup = true }
                )
                SubCategoryRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCategory = selectedSubCategory,
                    subCategoryMenuExpanded = subCategoryMenuExpanded,
                    toggleCategoryMenu = { subCategoryMenuExpanded = !subCategoryMenuExpanded },
                    selectedCategoryChange = { selectedSubCategory = it },
                    addEditCategory = { subCategoryAddEditDialogPopup = true }
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
                        onClick = {
                            uiAction(InventoryUiAction.OnAddItemClick)
                        }
                    ) {
                        Text(if (isEditing) stringResource(Res.string.update) else stringResource(Res.string.add))
                    }
                }
            }
            if (unitAddEditDialogPopup){
                AddEditItemUnitDialog(
                    itemUnit = null,
                    onDismiss = { unitAddEditDialogPopup = false },
                    onConfirm = { unitAddEditDialogPopup = false }
                )
            }
            if (categoryAddEditDialogPopup){
                AddEditCategoryDialog(
                    category = null,
                    onDismiss = { categoryAddEditDialogPopup = false },
                    onConfirm = { categoryAddEditDialogPopup = false }
                )
            }
            if (subCategoryAddEditDialogPopup){
                AddEditSubCategoryDialog(
                    category = null,
                    onDismiss = { subCategoryAddEditDialogPopup = false },
                    onConfirm = { subCategoryAddEditDialogPopup = false }
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
    addEditCategory: () -> Unit
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
                }
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
    addEditUnit: () -> Unit
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
                }
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
fun SubCategoryRow(
    modifier: Modifier,
    toggleCategoryMenu: () -> Unit = {},
    selectedCategory: SubCategory? = null,
    subCategoryMenuExpanded: Boolean = false,
    selectedCategoryChange: (SubCategory) -> Unit,
    addEditCategory: () -> Unit
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
                    Text(text = selectedCategory?.name ?: stringResource(Res.string.select_sub_category))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "ArrowDropdown")
                }
            }
            SubCategoryDropdown(
                isSubCategoryMenuVisible = subCategoryMenuExpanded,
                subCategoryMenuExpandedChange = toggleCategoryMenu,
                selectedSubCategoryChange = {
                    selectedCategoryChange(it)
                    toggleCategoryMenu()
                }
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
fun SubCategoryDropdown(
    isSubCategoryMenuVisible: Boolean,
    subCategoryMenuExpandedChange: () -> Unit,
    selectedSubCategoryChange: (SubCategory) -> Unit,
){
    DropdownMenu(
        expanded = isSubCategoryMenuVisible,
        onDismissRequest = subCategoryMenuExpandedChange,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        dummySubCategories.forEach { category ->
            DropdownMenuItem(
                text = { Text(text = category.name) },
                onClick = { selectedSubCategoryChange(category) }
            )
        }
    }
}

@Composable
fun ItemUnitDropdown(
    itemUnitMenuExpanded: Boolean,
    unitMenuExpandedChange: () -> Unit,
    selectedUnitChange: (ItemUnit) -> Unit
){
    DropdownMenu(
        expanded = itemUnitMenuExpanded,
        onDismissRequest = unitMenuExpandedChange,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        dummyItemUnits.forEach { itemUnits ->
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
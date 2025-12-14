package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.core.designsystem.components.TitleLargeText
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.SubCategory
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.add_sub_category
import pos_offline.composeapp.generated.resources.cancel
import pos_offline.composeapp.generated.resources.category_name
import pos_offline.composeapp.generated.resources.edit_sub_category
import pos_offline.composeapp.generated.resources.pick_parent_category
import pos_offline.composeapp.generated.resources.save
import pos_offline.composeapp.generated.resources.select_parent_category


@Composable
fun AddEditSubCategoryDialog(
    category: SubCategory?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val isEditing = category != null
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isParentCategoryExpanded by remember { mutableStateOf(false) }
    var selectedParentCategory by remember { mutableStateOf<Category?>(null) }



    Dialog(
        onDismissRequest = onDismiss
    ){
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isEditing){
                    TitleLargeText(title = stringResource(Res.string.edit_sub_category))
                }else{
                    TitleLargeText(title = stringResource(Res.string.add_sub_category))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = stringResource(Res.string.pick_parent_category), style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(2.dp))
                    ParentCategory(
                        modifier = Modifier.fillMaxWidth(),
                        toggleCategoryMenu = { isParentCategoryExpanded = !isParentCategoryExpanded },
                        selectedCategory = selectedParentCategory,
                        parentCategoryMenuExpanded = isParentCategoryExpanded,
                        selectedParentCategoryChange = { selectedParentCategory = it },
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTF(
                    value = "",
                    onValueChange = {},
                    placeholder = stringResource(Res.string.category_name),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Button(
                        onClick = onDismiss,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = stringResource(Res.string.cancel))

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        shape = MaterialTheme.shapes.small
                    ){
                        Text(text = stringResource(Res.string.save))
                    }
                }

            }
        }
    }
}
@Composable
fun ParentCategory(
    modifier: Modifier,
    toggleCategoryMenu: () -> Unit = {},
    selectedCategory: Category? = null,
    parentCategoryMenuExpanded: Boolean = false,
    selectedParentCategoryChange: (Category) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .clickable(
                    onClick = toggleCategoryMenu
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = selectedCategory?.name ?: stringResource(Res.string.select_parent_category))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "ArrowDropdown"
                )
            }
        }
        CategoryDropdown(
            categoryMenuExpanded = parentCategoryMenuExpanded,
            categoryMenuExpandedChange = toggleCategoryMenu,
            selectedCategoryChange = {
                selectedParentCategoryChange(it)
                toggleCategoryMenu()
            }
        )
    }
}
@Preview(name = "LightMode")
@Composable
fun SubCategoryDialogPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            AddEditSubCategoryDialog(
                onDismiss = {},
                onConfirm = {},
                category = null
            )
        }
    )
}
@Preview(name = "DarkMode")
@Composable
fun SubCategoryDialogDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            AddEditSubCategoryDialog(
                onDismiss = {},
                onConfirm = {},
                category = null
            )
        }
    )
}
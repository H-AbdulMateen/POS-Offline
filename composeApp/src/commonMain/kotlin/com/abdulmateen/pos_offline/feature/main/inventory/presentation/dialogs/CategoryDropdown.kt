package com.abdulmateen.pos_offline.feature.main.inventory.presentation.dialogs

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abdulmateen.pos_offline.feature.main.home.domain.models.Category
import com.abdulmateen.pos_offline.feature.main.home.domain.models.dummyCategories

@Composable
fun CategoryDropdown(
    categoryMenuExpanded: Boolean,
    categoryMenuExpandedChange: () -> Unit,
    selectedCategoryChange: (Category) -> Unit,
    list: List<Category>
){
    DropdownMenu(
        expanded = categoryMenuExpanded,
        onDismissRequest = categoryMenuExpandedChange,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        list.forEach { category ->
            DropdownMenuItem(
                text = { Text(text = category.name) },
                onClick = { selectedCategoryChange(category) }
            )
        }
    }
}
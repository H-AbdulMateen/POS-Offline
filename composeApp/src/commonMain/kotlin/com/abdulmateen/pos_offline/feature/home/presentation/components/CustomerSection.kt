package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomerSection(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Customer Info", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun CustomerSectionPreviewLight() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            CustomerSection()
        }
    )
}

@Preview(name = "Dark Mode")
@Composable
fun CustomerSectionPreviewDark() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            CustomerSection()
        }
    )
}


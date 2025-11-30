package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TitleLargeText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Preview(name = "LightMode", showBackground = true)
@Composable
fun TitleLargeTextPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            TitleLargeText(title = "POS Offline")
        }
    )
}
@Composable
fun TitleMediumText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Preview(name = "LightMode", showBackground = true)
@Composable
fun TitleMediumTextPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            TitleMediumText(title = "POS Offline")
        }
    )
}

@Composable
fun LabelMedium(
    label: String,
    modifier: Modifier = Modifier
){
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LabelMediumPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            LabelMedium(label = "POS Offline")
        }
    )
}
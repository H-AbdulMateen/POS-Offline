package com.abdulmateen.pos_offline.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.discount
import pos_offline.composeapp.generated.resources.sub_total
import pos_offline.composeapp.generated.resources.tax
import pos_offline.composeapp.generated.resources.total

@Composable
fun TotalCheckoutSection(
    modifier: Modifier = Modifier,
    subTotal: Double
){
    Card (
        modifier = modifier,
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            TotalSectionRow(
                label = stringResource(Res.string.sub_total).plus(":"),
                value = subTotal
            )
            HorizontalDivider()
            TotalSectionRow(
                label = stringResource(Res.string.discount).plus(":"),
                value = 0.0
            )
            HorizontalDivider()
            TotalSectionRow(
                label = stringResource(Res.string.tax).plus(":"),
                value = 0.0
            )
            HorizontalDivider()
            TotalSectionRow(
                label = stringResource(Res.string.total).plus(":"),
                value = subTotal
            )
        }
    }
}

@Composable
fun TotalSectionRow(
    label: String,
    value: Double
){

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp
        )
        Text(
            text = "$$value",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(name = "Light Mode")
@Composable
private fun TotalCheckoutSectionPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            TotalCheckoutSection(
                subTotal = 100.0
            )
        }
    )
}
@Preview(name = "Dark Mode")
@Composable
private fun TotalCheckoutSectionDarkPreview(){
    POSOfflineTheme(
        darkTheme = true,
        content = {
            TotalCheckoutSection(
                subTotal = 100.0
            )
        }
    )
}

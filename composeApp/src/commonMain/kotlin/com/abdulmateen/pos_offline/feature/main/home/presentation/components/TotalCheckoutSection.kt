package com.abdulmateen.pos_offline.feature.main.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.proceed_to_checkout
import pos_offline.composeapp.generated.resources.total

@Composable
fun TotalCheckoutSection(
    onProceedToCheckout: () -> Unit,
    modifier: Modifier = Modifier,
    totalAmount: Double
){
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(stringResource(Res.string.total).plus("Rs $totalAmount"), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onProceedToCheckout,
        ) { Text(stringResource(Res.string.proceed_to_checkout)) }
    }
}

@Preview(name = "Light Mode")
@Composable
private fun TotalCheckoutSectionPreview(){
    POSOfflineTheme(
        darkTheme = false,
        content = {
            TotalCheckoutSection(
                onProceedToCheckout = {},
                totalAmount = 100.0
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
                onProceedToCheckout = {},
                totalAmount = 100.0
            )
        }
    )
}

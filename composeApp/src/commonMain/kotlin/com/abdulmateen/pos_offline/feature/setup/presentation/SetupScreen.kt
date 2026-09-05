package com.abdulmateen.pos_offline.feature.setup.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.LogoImage
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.app_name

@Composable
fun SetupScreenRoot(
    onSetupComplete: () -> Unit
) {
    val viewModel = koinViewModel<SetupViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isSetupCompleted) {
        LaunchedEffect(Unit) {
            onSetupComplete()
        }
    }

    SetupScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onBrandNameChange = viewModel::onBrandNameChange,
        onBusinessNameChange = viewModel::onBusinessNameChange,
        onPhoneChange = viewModel::onPhoneChange,
        onCurrencySymbolChange = viewModel::onCurrencySymbolChange,
        onCompleteSetup = viewModel::completeSetup
    )
}

@Composable
fun SetupScreen(
    uiState: SetupUiState,
    onEmailChange: (String) -> Unit,
    onBrandNameChange: (String) -> Unit,
    onBusinessNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCurrencySymbolChange: (String) -> Unit,
    onCompleteSetup: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoImage(modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to ${stringResource(Res.string.app_name)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Let's set up your business",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTF(
                value = uiState.email,
                onValueChange = onEmailChange,
                placeholder = "Gmail Address (for backup)",
                modifier = Modifier.fillMaxWidth(),
                hasError = uiState.emailError != null,
                errorMessage = uiState.emailError ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTF(
                value = uiState.brandName,
                onValueChange = onBrandNameChange,
                placeholder = "Brand Name",
                modifier = Modifier.fillMaxWidth(),
                hasError = uiState.brandNameError != null,
                errorMessage = uiState.brandNameError ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTF(
                value = uiState.businessName,
                onValueChange = onBusinessNameChange,
                placeholder = "Business Name (appears on invoices)",
                modifier = Modifier.fillMaxWidth(),
                hasError = uiState.businessNameError != null,
                errorMessage = uiState.businessNameError ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTF(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                placeholder = "Business Phone",
                modifier = Modifier.fillMaxWidth(),
                hasError = uiState.phoneError != null,
                errorMessage = uiState.phoneError ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            var expanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.currencySymbol,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency Symbol") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(androidx.compose.material.icons.Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    listOf("$", "PKR", "INR", "€", "£", "¥").forEach { symbol ->
                        DropdownMenuItem(
                            text = { Text(symbol) },
                            onClick = {
                                onCurrencySymbolChange(symbol)
                                expanded = false
                            }
                        )
                    }
                }
                // Overlay a clickable box to trigger dropdown
                Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onCompleteSetup,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Complete Setup", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Preview
@Composable
fun SetupScreenPreview() {
    POSOfflineTheme(
        content = {
            SetupScreen(
                uiState = SetupUiState(),
                onEmailChange = {},
                onBrandNameChange = {},
                onBusinessNameChange = {},
                onPhoneChange = {},
                onCurrencySymbolChange = {},
                onCompleteSetup = {}
            )
        }
    )
}

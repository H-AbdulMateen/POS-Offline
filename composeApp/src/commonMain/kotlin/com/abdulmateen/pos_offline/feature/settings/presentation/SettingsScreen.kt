package com.abdulmateen.pos_offline.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.designsystem.components.OutlinedTF
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.logout
import pos_offline.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenRoot(
    onLogoutClick: () -> Unit
){
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onBrandNameChange = viewModel::onBrandNameChange,
        onBusinessNameChange = viewModel::onBusinessNameChange,
        onPhoneChange = viewModel::onPhoneChange,
        onCurrencySymbolChange = viewModel::onCurrencySymbolChange,
        onCustomizablePriceToggle = viewModel::onCustomizablePriceToggle,
        onSaveClick = viewModel::saveSettings,
        onLogoutClick = onLogoutClick,
        doLogoutUser = viewModel::doLogoutUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEmailChange: (String) -> Unit,
    onBrandNameChange: (String) -> Unit,
    onBusinessNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCurrencySymbolChange: (String) -> Unit,
    onCustomizablePriceToggle: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onLogoutClick: () -> Unit,
    doLogoutUser: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar("Settings saved successfully")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(stringResource(Res.string.settings)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Business Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    OutlinedTF(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        placeholder = "Gmail Address",
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTF(
                        value = uiState.brandName,
                        onValueChange = onBrandNameChange,
                        placeholder = "Brand Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTF(
                        value = uiState.businessName,
                        onValueChange = onBusinessNameChange,
                        placeholder = "Business Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTF(
                        value = uiState.phone,
                        onValueChange = onPhoneChange,
                        placeholder = "Business Phone",
                        modifier = Modifier.fillMaxWidth()
                    )

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
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
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
                        Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Customizable Price", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text("Allow changing product price at order creation", style = MaterialTheme.typography.bodySmall)
                        }
                        Checkbox(
                            checked = uiState.isCustomizablePriceEnabled,
                            onCheckedChange = onCustomizablePriceToggle
                        )
                    }

                    Button(
                        onClick = onSaveClick,
                        modifier = Modifier.align(Alignment.End),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Save Changes")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

//            Button(
//                onClick = {
//                    doLogoutUser()
//                    onLogoutClick()
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
//                shape = MaterialTheme.shapes.medium
//            ) {
//                Text(text = stringResource(Res.string.logout), modifier = Modifier.padding(vertical = 8.dp))
//            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    POSOfflineTheme(
        content = {
            SettingsScreen(
                uiState = SettingsUiState(),
                onEmailChange = {},
                onBrandNameChange = {},
                onBusinessNameChange = {},
                onPhoneChange = {},
                onCurrencySymbolChange = {},
                onCustomizablePriceToggle = {},
                onSaveClick = {},
                onLogoutClick = {},
                doLogoutUser = {}
            )
        }
    )
}

package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.stevdza_san.library.component.CountryPickerDialog
import com.stevdza_san.library.component.CountryPickerField
import com.stevdza_san.library.domain.Country
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.ic_visibility
import pos_offline.composeapp.generated.resources.ic_visibility_off
import pos_offline.composeapp.generated.resources.search


@Composable
fun OutlinedTF(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    hasError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(text = placeholder) },
        isError = hasError,
        supportingText = {
            if (hasError) {
                Text(text = errorMessage)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine
    )
}

@Preview
@Composable
fun OutlinedTFPreview() {
    OutlinedTF(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.search),
    hasError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(text = placeholder) },
        isError = hasError,
        supportingText = {
            if (hasError) {
                Text(text = errorMessage)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    )
}

@Preview
@Composable
fun SearchFieldPreview() {
    SearchField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OutlinedTFPassword(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    hasError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Password,
){
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { Text(text = placeholder) },
        isError = hasError,
        supportingText = {
            if (hasError) {
                Text(text = errorMessage)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(if (passwordVisibility) Res.drawable.ic_visibility_off else Res.drawable.ic_visibility),
                contentDescription = "VisibilityIcon",
                modifier = Modifier.clickable(
                    onClick = { passwordVisibility = !passwordVisibility }
                ).size(24.dp)
            )
        }
    )
}

@Preview
@Composable
fun OutlinedTFPasswordPreview() {
    OutlinedTFPassword(
        value = "123",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OutlinedPhoneTF(
    modifier: Modifier = Modifier,
    phoneNumber: String = "",
    onPhoneNumberChange: (String) -> Unit = {},
    placeholder: String = "",
){
    var selectedCountry by remember { mutableStateOf(Country.Serbia) }
    var showDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(visible = showDialog) {
        CountryPickerDialog(
            selectedCountry = selectedCountry,
            onConfirmClick = { country ->
                selectedCountry = country
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = Modifier.clickable(
                onClick = { showDialog = true }
            )
                .padding(bottom = 8.dp),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(selectedCountry.flag),
                    contentDescription = "Flag",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "+${selectedCountry.dialCode}")
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTF(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.weight(.1f),
            placeholder = placeholder,
        )
    }
}
@Preview
@Composable
fun CountryPickerFieldPreview(){
    OutlinedPhoneTF(
        modifier = Modifier.fillMaxWidth(),
        phoneNumber = "",
        onPhoneNumberChange = {}
    )
}

@Composable
fun OutlinedTFDate(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {  },
        modifier = modifier.clickable(
            onClick = onClick
        ),
        placeholder = { Text(text = placeholder) },
        enabled = false,
        trailingIcon = {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar",
                modifier = Modifier
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Black,
            disabledTrailingIconColor = Black,
            disabledPlaceholderColor = Black,
            disabledBorderColor = LightGray,
        )
    )
}

@Preview
@Composable
fun DatePickerOutlinedTFPreview(){
    OutlinedTFDate(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onClick = {}
    )
}
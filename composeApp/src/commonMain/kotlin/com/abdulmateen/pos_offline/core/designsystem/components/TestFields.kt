package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.ic_visibility
import pos_offline.composeapp.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
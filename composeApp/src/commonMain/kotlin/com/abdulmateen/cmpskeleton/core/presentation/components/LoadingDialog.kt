package com.abdulmateen.cmpskeleton.core.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit
){
    if (isVisible){
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ){
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview(){
    LoadingDialog(
        isVisible = true,
        onDismissRequest = {}
    )
}
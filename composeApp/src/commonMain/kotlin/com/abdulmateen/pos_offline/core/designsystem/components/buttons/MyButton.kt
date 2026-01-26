package com.abdulmateen.pos_offline.core.designsystem.components.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.abdulmateen.pos_offline.ui.theme.POSOfflineTheme

@Composable
fun MyButtonDestructive(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        shape = MaterialTheme.shapes.medium
    ){
        Text(
            text = text
        )
    }
}




@Composable
fun MyButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.medium
    ){
        Text(
            text = text
        )
    }
}

@Preview
@Composable
fun MyButtonDestructivePreview() {
    POSOfflineTheme(
        darkTheme = true,
        content = {
            MyButtonDestructive(
                text = "Destructive Button",
                onClick = {}
            )
        }
    )
}

@Preview
@Composable
fun MyButtonPrimaryPreview() {
    POSOfflineTheme(
        darkTheme = false,
        content = {
            MyButtonPrimary(
                text = "Primary Button",
                onClick = {}
            )
        }
    )
}
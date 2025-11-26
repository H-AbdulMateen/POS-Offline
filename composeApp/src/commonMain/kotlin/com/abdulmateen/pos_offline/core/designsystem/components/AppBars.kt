package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBarTitle(
    title: String,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = White
){
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        }
    )
}

@Preview
@Composable
fun CenteredTopBarTitlePreview(){
    CenteredTopBarTitle(
        title = "Title"
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBarNavTitle(
    onBackClick: () -> Unit,
    title: String,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = White
){
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            BackIconButton(onClick = onBackClick)
        }
    )
}

@Preview
@Composable
fun CenteredTopBarNavTitlePreview(){
    CenteredTopBarNavTitle(
        onBackClick = {},
        title = "Title"
    )
}
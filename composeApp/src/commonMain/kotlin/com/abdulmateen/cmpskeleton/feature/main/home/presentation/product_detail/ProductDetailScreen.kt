package com.abdulmateen.cmpskeleton.feature.main.home.presentation.product_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cmpskeleton.composeapp.generated.resources.Res
import cmpskeleton.composeapp.generated.resources.product_detail
import com.abdulmateen.cmpskeleton.core.presentation.components.BackIconButton
import com.abdulmateen.cmpskeleton.core.presentation.components.CenteredTopBarNavTitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProductDetailScreenRoot(
    onBackClick: () -> Unit
) {
    ProductDetailScreen(
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenteredTopBarNavTitle(
                onBackClick = onBackClick,
                title = stringResource(Res.string.product_detail),
            ) }
    ) {innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column {
                Text(text = "Product Detail Screen")
            }
        }
    }
}

@Preview
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        onBackClick = {}
    )
}
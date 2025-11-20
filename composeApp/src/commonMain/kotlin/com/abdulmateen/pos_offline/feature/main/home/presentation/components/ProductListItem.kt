package com.abdulmateen.pos_offline.feature.main.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.book_error
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.abdulmateen.pos_offline.feature.main.home.domain.Product
import com.abdulmateen.pos_offline.feature.main.home.domain.Rating
import com.abdulmateen.pos_offline.feature.main.home.presentation.order.ProductListUiAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProductListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    item: Product,
    uiAction: (ProductListUiAction) -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = White
        )
    ) {
        Box(
            modifier = Modifier.height(150.dp)
        ) {

            var imageLoadResult by remember {
                mutableStateOf<Result<Painter>?>(null)
            }
            val painter = rememberAsyncImagePainter(
                model = item.image,
                onSuccess = {
                    imageLoadResult =
                        if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                            Result.success(it.painter)
                        } else {
                            Result.failure(Exception("Invalid image size"))
                        }
                },
                onError = {
                    it.result.throwable.printStackTrace()
                    imageLoadResult = Result.failure(it.result.throwable)
                }
            )

            val painterState by painter.state.collectAsStateWithLifecycle()
            val transition by animateFloatAsState(
                targetValue = if (painterState is AsyncImagePainter.State.Success) {
                    1f
                } else {
                    0f
                },
                animationSpec = tween(durationMillis = 800)
            )

            when (val result = imageLoadResult) {
                null -> CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
                else -> {
                    Image(
                        painter = if (result.isSuccess) painter else {
                            painterResource(Res.drawable.book_error)
                        },
                        contentDescription = item.title,
                        contentScale = if (result.isSuccess) {
                            ContentScale.Fit
                        } else {
                            ContentScale.Fit
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .align(alignment = Alignment.Center)
                            .aspectRatio(
                                ratio = 0.65f,
                                matchHeightConstraintsFirst = true,
                            )
                            .graphicsLayer {
                                rotationX = (1f - transition) * 30f
                                val scale = 0.8f + (0.2f * transition)
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                }
            }
            FilledIconButton(
                onClick = {
                    if (item.isFavourite){
                        uiAction(ProductListUiAction.RemoveFromFavourite(id = item.id))
                    }else{
                        uiAction(ProductListUiAction.MarkAsFavourite(productId = item.id))
                    }
                },
                content = {
                    Icon(
                        imageVector = if (item.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "HeartIcon",
                        tint = if (item.isFavourite) Red else Black,
                        modifier = Modifier.padding(2.dp)
                    )
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = LightGray.copy(alpha = 0.3f),
                    contentColor = Black
                ),
                modifier = Modifier.size(18.dp).align(alignment = Alignment.TopEnd)
            )
        }
        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Rs ${item.price}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Black))
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "CartIcon", tint = Red)
            }
        }
    }
}

@Preview
@Composable
private fun ProductListItemPreview() {
    ProductListItem(
        onClick = {},
        item = Product(
            id = 1,
            title = "Product 1",
            price = 10.0,
            description = "Description 1",
            category = "Category 1",
            image = "",
            rating = Rating(
                rate = 4.5,
                count = 100
            )
        ),
        uiAction = {}
        )
}
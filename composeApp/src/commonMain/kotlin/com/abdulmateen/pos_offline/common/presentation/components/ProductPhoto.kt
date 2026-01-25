package com.abdulmateen.pos_offline.common.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abdulmateen.pos_offline.core.presentation.rememberBitmapFromBytes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProductPhoto(
    photoBytes: ByteArray?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {
    val bitmap = rememberBitmapFromBytes(photoBytes)
    val photoModifier = modifier.clip(RoundedCornerShape(35))

    if(bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = photoModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = photoModifier
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview
@Composable
fun ProductPhotoPreview() {
    ProductPhoto(
        photoBytes = null,
        contentDescription = "Product photo"
    )
}
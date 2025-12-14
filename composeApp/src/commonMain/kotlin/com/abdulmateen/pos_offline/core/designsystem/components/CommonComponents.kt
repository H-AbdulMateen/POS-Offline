package com.abdulmateen.pos_offline.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pos_offline.composeapp.generated.resources.Res
import pos_offline.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogoImage(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(resource = Res.drawable.compose_multiplatform),
        contentDescription = null,
        modifier = modifier
    )
}

@Preview
@Composable
fun LogoImagePreview(){
    LogoImage(
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LocalImageWidget(
    modifier: Modifier = Modifier,
    selectedImage: ImageBitmap?
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        selectedImage?.let {
            Image(
                bitmap = it,
                contentDescription = "UserProfileImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun LocalImageWidgetPreview(){
        LocalImageWidget(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(LightGray),
            selectedImage = null
        )
}

@Composable
fun ErrorSurface(
    modifier: Modifier = Modifier,
    errorMessage: String
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.error
    ){
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.labelLarge
                .copy(
                    color = MaterialTheme.colorScheme.onError,
                    textAlign = TextAlign.Center
                ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun ErrorMessagePreview(){
    ErrorSurface(
        modifier = Modifier.fillMaxWidth(),
        errorMessage = "Error surface!"
    )
}

@Composable
fun SimpleBtn(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color,
    contentColor: Color
){
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ){
        if (loading){
            CircularProgressIndicator()
        }else{
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun SimpleBtnPreview(){
    SimpleBtn(
        modifier = Modifier.fillMaxWidth(),
        text = "Simple Button",
        onClick = {},
        containerColor = Color.Green,
        contentColor = Color.White,
        loading = false
    )
}

@Composable
fun BackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier
    ){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "BackArrowIcon"
        )
    }
}

@Preview
@Composable
fun BackIconButtonPreview(){
    BackIconButton(
        onClick = {}
    )
}

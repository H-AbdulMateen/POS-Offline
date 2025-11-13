package com.abdulmateen.cmpskeleton.feature.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmpskeleton.composeapp.generated.resources.Res
import cmpskeleton.composeapp.generated.resources.chevron_left
import cmpskeleton.composeapp.generated.resources.date_of_birth
import cmpskeleton.composeapp.generated.resources.sign_up
import com.abdulmateen.cmpskeleton.core.presentation.components.BackIconButton
import com.abdulmateen.cmpskeleton.core.presentation.components.LocalImageWidget
import com.abdulmateen.cmpskeleton.core.presentation.components.OutlinedTF
import com.abdulmateen.cmpskeleton.core.presentation.components.OutlinedTFDate
import com.abdulmateen.cmpskeleton.core.presentation.components.SimpleBtn
import com.abdulmateen.cmpskeleton.core.presentation.components.WheelDateTimePickerDialog
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun SignUpScreenRoot(
    onBackClicked: () -> Unit
){
    val viewModel: SignUpViewModel = koinViewModel()
    SignUpScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        uiAction = viewModel::uiAction,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    uiAction: (SignUpUiAction) -> Unit,
    onBackClicked: () -> Unit
) {
    val imageCropper = rememberImageCropper()
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var openImagePicker by remember { mutableStateOf(value = false) }

    CMPImagePickNCropDialog(
        imageCropper = imageCropper,
        openImagePicker = openImagePicker,
        imagePickerDialogHandler = {
            openImagePicker = it
        },
        selectedImageCallback = {
            selectedImage = it
        },
        selectedImageFileCallback = {}
    )
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .consumeWindowInsets(WindowInsets.statusBars)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BackIconButton(
                    onClick = onBackClicked
                )

                Text(
                    text = stringResource(Res.string.sign_up),
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                )
                Box{}
            }
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                color = White,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text(text = "Create your account")
                    //User Profile Image
                    LocalImageWidget(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                            .clickable { openImagePicker = true }
                            .background(LightGray.takeIf { selectedImage == null } ?: Transparent),
                        selectedImage = selectedImage
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //Name
                    OutlinedTF(
                        value = uiState.fullName,
                        onValueChange = { uiAction(SignUpUiAction.UpdateFullName(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    //Email
                    OutlinedTF(
                        value = uiState.email,
                        onValueChange = { uiAction(SignUpUiAction.UpdateEmail(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    //Date of Birth
                    OutlinedTFDate(
                        value = uiState.dateOfBirth,
                        placeholder = stringResource(Res.string.date_of_birth),
                        onClick = { uiAction(SignUpUiAction.ToggleDatePickerDialogVisibility) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SimpleBtn(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {uiAction(SignUpUiAction.SignUp)},
                        text = stringResource(Res.string.sign_up),
                        contentColor = White,
                        containerColor = MaterialTheme.colorScheme.primary,
                        loading = uiState.isLoading
                    )







                }
            }
        }
    }
    if (uiState.datePickerDialogVisibility) {
        WheelDateTimePickerDialog(
            showDatePicker = uiState.datePickerDialogVisibility,
            toggleDatePicker = { uiAction(SignUpUiAction.ToggleDatePickerDialogVisibility) },
            onDateSelection = { uiAction(SignUpUiAction.UpdateDateOfBirth(it)) }
        )
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        uiState = SignUpUiState(),
        uiAction = {},
        onBackClicked = {}
    )
}
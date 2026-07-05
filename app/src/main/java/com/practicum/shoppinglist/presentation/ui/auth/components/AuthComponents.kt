package com.practicum.shoppinglist.presentation.ui.auth.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.colors
import com.practicum.shoppinglist.presentation.theme.isDarkTheme
import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.AuthMessageType

@Composable
fun AuthScreenContainer(
    @StringRes titleResId: Int,
    @StringRes subtitleResId: Int,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.Auth.screenHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (onBackClick != null) {
            AuthToolbar(onBackClick = onBackClick)
        } else {
            Spacer(modifier = Modifier.height(Dimens.Auth.logoTopPadding))
        }
        Column(
            modifier = Modifier.widthIn(max = Dimens.Auth.contentMaxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(
                    id = if (MaterialTheme.isDarkTheme) {
                        R.drawable.onboarding_logo_dark
                    } else {
                        R.drawable.onboarding_logo
                    },
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Auth.logoHorizontalPadding)
                    .aspectRatio(Dimens.Auth.logoAspectRatio),
            )
            Spacer(modifier = Modifier.height(Dimens.Auth.titleTopPadding))
            Text(
                text = stringResource(id = titleResId),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.Auth.subtitleTopPadding))
            Text(
                text = stringResource(id = subtitleResId),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.Auth.fieldTopPadding))
            content()
        }
    }
}

@Composable
private fun AuthToolbar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.Auth.toolbarTopPadding)
            .height(Dimens.Auth.toolbarHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.offset(x = -Dimens.Auth.backButtonStartOffset),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.auth_back_content_description),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun AuthEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        labelResId = R.string.auth_email_label,
        isError = isError,
        errorResId = R.string.auth_error_invalid_email,
        keyboardType = KeyboardType.Email,
        autofillContentType = ContentType.EmailAddress,
        modifier = modifier,
    )
}

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onVisibilityClick: () -> Unit,
    isError: Boolean,
    @StringRes errorResId: Int,
    modifier: Modifier = Modifier,
    @StringRes labelResId: Int = R.string.auth_password_label,
    autofillContentType: ContentType? = ContentType.Password,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.passwordFieldModifier(autofillContentType),
        label = { Text(text = stringResource(id = labelResId)) },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            AuthPasswordTrailingActions(
                hasValue = value.isNotEmpty(),
                isPasswordVisible = isPasswordVisible,
                onClearClick = { onValueChange("") },
                onVisibilityClick = onVisibilityClick,
            )
        },
        supportingText = {
            if (isError) {
                Text(text = stringResource(id = errorResId))
            }
        },
    )
}

private fun Modifier.passwordFieldModifier(autofillContentType: ContentType?): Modifier {
    val fieldModifier = fillMaxWidth()
    return if (autofillContentType == null) {
        fieldModifier
    } else {
        fieldModifier.semantics { contentType = autofillContentType }
    }
}

@Composable
private fun AuthPasswordTrailingActions(
    hasValue: Boolean,
    isPasswordVisible: Boolean,
    onClearClick: () -> Unit,
    onVisibilityClick: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (hasValue) {
            AuthClearButton(
                onClick = onClearClick,
                modifier = Modifier.offset(x = Dimens.Auth.passwordClearButtonEndOffset),
            )
        }
        IconButton(onClick = onVisibilityClick) {
            Icon(
                imageVector = passwordVisibilityIcon(isPasswordVisible),
                contentDescription = stringResource(
                    id = passwordVisibilityContentDescription(isPasswordVisible),
                ),
            )
        }
    }
}

private fun passwordVisibilityIcon(isPasswordVisible: Boolean): ImageVector {
    return if (isPasswordVisible) {
        Icons.Default.VisibilityOff
    } else {
        Icons.Default.Visibility
    }
}

@StringRes
private fun passwordVisibilityContentDescription(isPasswordVisible: Boolean): Int {
    return if (isPasswordVisible) {
        R.string.auth_password_hidden_content_description
    } else {
        R.string.auth_password_visible_content_description
    }
}

@Composable
fun AuthPrimaryButton(
    @StringRes textResId: Int,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.Auth.buttonLoaderSize),
                color = MaterialTheme.colors.authButtonLoader,
                strokeWidth = Dimens.Auth.buttonLoaderStrokeWidth,
            )
        } else {
            Text(text = stringResource(id = textResId))
        }
    }
}

@Composable
fun AuthMessageText(
    message: AuthMessage?,
    modifier: Modifier = Modifier,
) {
    if (message == null) {
        return
    }
    Text(
        text = stringResource(id = message.stringResId),
        color = message.textColor(),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.Auth.messageTopPadding),
    )
}

@Composable
fun AuthSecondaryActions(
    onRegisterClick: () -> Unit,
    onRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Auth.secondaryActionTopPadding),
    ) {
        TextButton(onClick = onRegisterClick) {
            Text(text = stringResource(id = R.string.auth_go_to_register))
        }
        TextButton(onClick = onRecoveryClick) {
            Text(text = stringResource(id = R.string.auth_go_to_recovery))
        }
    }
}

@Composable
private fun AuthMessage.textColor(): Color {
    return when (type) {
        AuthMessageType.Error -> MaterialTheme.colorScheme.error
        AuthMessageType.Success -> MaterialTheme.colorScheme.primary
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelResId: Int,
    isError: Boolean,
    @StringRes errorResId: Int,
    keyboardType: KeyboardType,
    autofillContentType: ContentType,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentType = autofillContentType },
        label = { Text(text = stringResource(id = labelResId)) },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = {
            if (value.isNotEmpty()) {
                AuthClearButton(onClick = { onValueChange("") })
            }
        },
        supportingText = {
            if (isError) {
                Text(text = stringResource(id = errorResId))
            }
        },
    )
}

@Composable
private fun AuthClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.main_search_clear),
        )
    }
}

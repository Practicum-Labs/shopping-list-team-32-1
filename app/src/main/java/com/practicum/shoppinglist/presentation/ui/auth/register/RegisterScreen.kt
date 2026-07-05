package com.practicum.shoppinglist.presentation.ui.auth.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.presentation.theme.Dimens
import com.practicum.shoppinglist.presentation.theme.Theme
import com.practicum.shoppinglist.presentation.ui.auth.AuthMessage
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthEmailField
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthMessageText
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthPasswordField
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthPrimaryButton
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthScreenContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoute(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatedPasswordChange = viewModel::onRepeatedPasswordChange,
        onPasswordVisibilityClick = viewModel::onPasswordVisibilityClick,
        onRepeatedPasswordVisibilityClick = viewModel::onRepeatedPasswordVisibilityClick,
        onRegisterClick = { viewModel.onRegisterClick(onSuccess = onRegisterSuccess) },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onRepeatedPasswordVisibilityClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    AuthScreenContainer(
        titleResId = R.string.auth_register_title,
        subtitleResId = R.string.auth_register_subtitle,
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        AuthEmailField(
            value = uiState.email,
            onValueChange = onEmailChange,
            isError = uiState.isEmailErrorVisible,
        )
        Spacer(modifier = Modifier.height(Dimens.Auth.fieldSpacing))
        AuthPasswordField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            isPasswordVisible = uiState.isPasswordVisible,
            onVisibilityClick = onPasswordVisibilityClick,
            isError = uiState.isPasswordErrorVisible,
            errorResId = R.string.auth_error_password_requirements,
            autofillContentType = ContentType.NewPassword,
        )
        Spacer(modifier = Modifier.height(Dimens.Auth.fieldSpacing))
        AuthPasswordField(
            value = uiState.repeatedPassword,
            onValueChange = onRepeatedPasswordChange,
            isPasswordVisible = uiState.isRepeatedPasswordVisible,
            onVisibilityClick = onRepeatedPasswordVisibilityClick,
            isError = uiState.isRepeatedPasswordErrorVisible,
            errorResId = R.string.auth_error_password_mismatch,
            labelResId = R.string.auth_repeat_password_label,
            autofillContentType = ContentType.NewPassword,
        )
        AuthMessageText(message = uiState.message)
        Spacer(modifier = Modifier.height(Dimens.Auth.buttonTopPadding))
        AuthPrimaryButton(
            textResId = R.string.auth_register_button,
            enabled = uiState.canSubmit,
            isLoading = uiState.isLoading,
            onClick = {
                keyboardController?.hide()
                onRegisterClick()
            },
        )
    }
}

@Preview(name = "Register", showBackground = true)
@Composable
private fun RegisterScreenPreview() {
    Theme {
        RegisterScreen(
            uiState = RegisterUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
                repeatedPassword = PREVIEW_PASSWORD,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onRepeatedPasswordChange = {},
            onPasswordVisibilityClick = {},
            onRepeatedPasswordVisibilityClick = {},
            onRegisterClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Register loading", showBackground = true)
@Composable
private fun RegisterScreenLoadingPreview() {
    Theme {
        RegisterScreen(
            uiState = RegisterUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
                repeatedPassword = PREVIEW_PASSWORD,
                isLoading = true,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onRepeatedPasswordChange = {},
            onPasswordVisibilityClick = {},
            onRepeatedPasswordVisibilityClick = {},
            onRegisterClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Register error dark", showBackground = true)
@Composable
private fun RegisterScreenErrorDarkPreview() {
    Theme(darkTheme = true) {
        RegisterScreen(
            uiState = RegisterUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
                repeatedPassword = "Password2!",
                message = AuthMessage.UserAlreadyExists,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onRepeatedPasswordChange = {},
            onPasswordVisibilityClick = {},
            onRepeatedPasswordVisibilityClick = {},
            onRegisterClick = {},
            onBackClick = {},
        )
    }
}

private const val PREVIEW_EMAIL = "student@example.com"
private const val PREVIEW_PASSWORD = "Password1!"

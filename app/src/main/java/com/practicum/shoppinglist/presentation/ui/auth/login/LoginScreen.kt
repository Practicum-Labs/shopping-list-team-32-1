package com.practicum.shoppinglist.presentation.ui.auth.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthSecondaryActions
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisibilityClick = viewModel::onPasswordVisibilityClick,
        onLoginClick = { viewModel.onLoginClick(onSuccess = onLoginSuccess) },
        onRegisterClick = onRegisterClick,
        onRecoveryClick = onRecoveryClick,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoveryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    AuthScreenContainer(
        titleResId = R.string.auth_login_title,
        subtitleResId = R.string.auth_login_subtitle,
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
            errorResId = R.string.auth_error_login_password,
        )
        AuthMessageText(message = uiState.message)
        Spacer(modifier = Modifier.height(Dimens.Auth.buttonTopPadding))
        AuthPrimaryButton(
            textResId = R.string.auth_login_button,
            enabled = uiState.canSubmit,
            isLoading = uiState.isLoading,
            onClick = {
                keyboardController?.hide()
                onLoginClick()
            },
        )
        AuthSecondaryActions(
            onRegisterClick = onRegisterClick,
            onRecoveryClick = onRecoveryClick,
        )
    }
}

@Preview(name = "Login", showBackground = true)
@Composable
private fun LoginScreenPreview() {
    Theme {
        LoginScreen(
            uiState = LoginUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityClick = {},
            onLoginClick = {},
            onRegisterClick = {},
            onRecoveryClick = {},
        )
    }
}

@Preview(name = "Login loading", showBackground = true)
@Composable
private fun LoginScreenLoadingPreview() {
    Theme {
        LoginScreen(
            uiState = LoginUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
                isLoading = true,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityClick = {},
            onLoginClick = {},
            onRegisterClick = {},
            onRecoveryClick = {},
        )
    }
}

@Preview(name = "Login error dark", showBackground = true)
@Composable
private fun LoginScreenErrorDarkPreview() {
    Theme(darkTheme = true) {
        LoginScreen(
            uiState = LoginUiState(
                email = PREVIEW_EMAIL,
                password = PREVIEW_PASSWORD,
                message = AuthMessage.InvalidCredentials,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityClick = {},
            onLoginClick = {},
            onRegisterClick = {},
            onRecoveryClick = {},
        )
    }
}

private const val PREVIEW_EMAIL = "student@example.com"
private const val PREVIEW_PASSWORD = "Password1!"

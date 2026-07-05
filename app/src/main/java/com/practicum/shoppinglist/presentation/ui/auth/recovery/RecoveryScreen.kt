package com.practicum.shoppinglist.presentation.ui.auth.recovery

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
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthPrimaryButton
import com.practicum.shoppinglist.presentation.ui.auth.components.AuthScreenContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecoveryRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecoveryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecoveryScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onRecoverClick = viewModel::onRecoverClick,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun RecoveryScreen(
    uiState: RecoveryUiState,
    onEmailChange: (String) -> Unit,
    onRecoverClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    AuthScreenContainer(
        titleResId = R.string.auth_recovery_title,
        subtitleResId = R.string.auth_recovery_subtitle,
        onBackClick = onBackClick,
        modifier = modifier,
    ) {
        AuthEmailField(
            value = uiState.email,
            onValueChange = onEmailChange,
            isError = uiState.isEmailErrorVisible,
        )
        AuthMessageText(message = uiState.message)
        Spacer(modifier = Modifier.height(Dimens.Auth.buttonTopPadding))
        AuthPrimaryButton(
            textResId = R.string.auth_recovery_button,
            enabled = uiState.canSubmit,
            isLoading = uiState.isLoading,
            onClick = {
                keyboardController?.hide()
                onRecoverClick()
            },
        )
    }
}

@Preview(name = "Recovery", showBackground = true)
@Composable
private fun RecoveryScreenPreview() {
    Theme {
        RecoveryScreen(
            uiState = RecoveryUiState(email = PREVIEW_EMAIL),
            onEmailChange = {},
            onRecoverClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Recovery loading", showBackground = true)
@Composable
private fun RecoveryScreenLoadingPreview() {
    Theme {
        RecoveryScreen(
            uiState = RecoveryUiState(
                email = PREVIEW_EMAIL,
                isLoading = true,
            ),
            onEmailChange = {},
            onRecoverClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Recovery success dark", showBackground = true)
@Composable
private fun RecoveryScreenSuccessDarkPreview() {
    Theme(darkTheme = true) {
        RecoveryScreen(
            uiState = RecoveryUiState(
                email = PREVIEW_EMAIL,
                message = AuthMessage.RecoverySuccess,
            ),
            onEmailChange = {},
            onRecoverClick = {},
            onBackClick = {},
        )
    }
}

private const val PREVIEW_EMAIL = "student@example.com"

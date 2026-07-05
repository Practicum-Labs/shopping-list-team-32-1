package com.practicum.shoppinglist.presentation.ui.auth

import androidx.annotation.StringRes
import com.practicum.shoppinglist.R
import com.practicum.shoppinglist.domain.model.AuthError

enum class AuthMessage(
    @get:StringRes val stringResId: Int,
    val type: AuthMessageType = AuthMessageType.Error,
) {
    InvalidEmail(R.string.auth_error_invalid_email),
    WeakPassword(R.string.auth_error_weak_password),
    UserAlreadyExists(R.string.auth_error_user_exists),
    InvalidCredentials(R.string.auth_error_invalid_credentials),
    Unauthorized(R.string.auth_error_unauthorized),
    Network(R.string.auth_error_network),
    Server(R.string.auth_error_server),
    Unknown(R.string.auth_error_unknown),
    RecoverySuccess(
        stringResId = R.string.auth_recovery_success,
        type = AuthMessageType.Success,
    ),
}

enum class AuthMessageType {
    Error,
    Success,
}

fun Throwable.toAuthMessage(): AuthMessage {
    return when (this) {
        is AuthError.InvalidEmail -> AuthMessage.InvalidEmail
        is AuthError.WeakPassword -> AuthMessage.WeakPassword
        is AuthError.UserAlreadyExists -> AuthMessage.UserAlreadyExists
        is AuthError.InvalidCredentials -> AuthMessage.InvalidCredentials
        is AuthError.Unauthorized -> AuthMessage.Unauthorized
        is AuthError.Network -> AuthMessage.Network
        is AuthError.Server -> AuthMessage.Server
        else -> AuthMessage.Unknown
    }
}

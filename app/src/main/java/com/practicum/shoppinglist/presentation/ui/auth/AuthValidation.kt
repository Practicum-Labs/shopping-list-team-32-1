package com.practicum.shoppinglist.presentation.ui.auth

import android.util.Patterns

object AuthValidation {
    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getPasswordErrors(password: String): Set<PasswordRequirement> {
        return PasswordRequirement.entries.filterTo(mutableSetOf()) { requirement ->
            when (requirement) {
                PasswordRequirement.MinLength -> password.length < MIN_PASSWORD_LENGTH
                PasswordRequirement.Lowercase -> password.none { char -> char.isLowerCase() }
                PasswordRequirement.Uppercase -> password.none { char -> char.isUpperCase() }
                PasswordRequirement.Digit -> password.none { char -> char.isDigit() }
                PasswordRequirement.Special -> password.none { char -> !char.isLetterOrDigit() }
            }
        }
    }

    const val MIN_PASSWORD_LENGTH = 7
}

enum class PasswordRequirement {
    MinLength,
    Lowercase,
    Uppercase,
    Digit,
    Special,
}

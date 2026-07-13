package com.practicum.shoppinglist.presentation.ui.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthValidationTest {

    @Test
    fun `возвращает все недостающие требования для слабого пароля`() {
        val result = AuthValidation.getPasswordErrors(WEAK_PASSWORD)

        assertEquals(
            setOf(
                PasswordRequirement.MinLength,
                PasswordRequirement.Uppercase,
                PasswordRequirement.Digit,
                PasswordRequirement.Special,
            ),
            result,
        )
    }

    @Test
    fun `не возвращает ошибок для валидного пароля`() {
        val result = AuthValidation.getPasswordErrors(VALID_PASSWORD)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `считает корректный email валидным`() {
        val result = AuthValidation.isEmailValid(VALID_EMAIL)

        assertTrue(result)
    }

    @Test
    fun `считает email без домена невалидным`() {
        val result = AuthValidation.isEmailValid(INVALID_EMAIL)

        assertEquals(false, result)
    }

    @Test
    fun `возвращает ошибку если в пароле нет заглавной буквы`() {
        val result = AuthValidation.getPasswordErrors(PASSWORD_WITHOUT_UPPERCASE)

        assertEquals(setOf(PasswordRequirement.Uppercase), result)
    }

    @Test
    fun `возвращает ошибку если в пароле нет цифры`() {
        val result = AuthValidation.getPasswordErrors(PASSWORD_WITHOUT_DIGIT)

        assertEquals(setOf(PasswordRequirement.Digit), result)
    }

    @Test
    fun `возвращает ошибку если в пароле нет спецсимвола`() {
        val result = AuthValidation.getPasswordErrors(PASSWORD_WITHOUT_SPECIAL)

        assertEquals(setOf(PasswordRequirement.Special), result)
    }

    private companion object {
        const val WEAK_PASSWORD = "abc"
        const val VALID_PASSWORD = "Password1!"
        const val VALID_EMAIL = "student@example.com"
        const val INVALID_EMAIL = "student"
        const val PASSWORD_WITHOUT_UPPERCASE = "password1!"
        const val PASSWORD_WITHOUT_DIGIT = "Password!"
        const val PASSWORD_WITHOUT_SPECIAL = "Password1"
    }
}

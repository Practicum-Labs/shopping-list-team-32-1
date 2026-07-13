package com.practicum.shoppinglist.domain.usecase

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeUseCasesTest {

    private val themeRepository = FakeThemeRepository()

    @Test
    fun `наблюдает за настройкой темной темы`() = runTest {
        val useCase = ObserveDarkThemeUseCase(themeRepository)

        useCase().test {
            assertEquals(null, awaitItem())

            themeRepository.isDarkTheme.value = true

            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `сохраняет настройку темной темы`() = runTest {
        val useCase = SetDarkThemeUseCase(themeRepository)

        useCase(true)

        assertEquals(true, themeRepository.savedThemeValues.single())
    }
}

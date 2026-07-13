package com.practicum.shoppinglist.data.repository

import app.cash.turbine.test
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.domain.model.AuthSession
import com.practicum.shoppinglist.domain.model.ShoppingList
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingListRepositoryImplTest {

    private val shoppingListDao = FakeShoppingListDao()
    private val authRepository = FakeAuthRepository()
    private val repository = ShoppingListRepositoryImpl(
        shoppingListDao = shoppingListDao,
        authRepository = authRepository,
    )

    @Test
    fun `возвращает пустой список если пользователь не авторизован`() = runTest {
        repository.observeShoppingLists().test {
            assertEquals(emptyList<ShoppingList>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `наблюдает списки текущего пользователя`() = runTest {
        val entity = shoppingListEntity()

        repository.observeShoppingLists().test {
            assertEquals(emptyList<ShoppingList>(), awaitItem())
            authRepository.emitSession(authSession())
            shoppingListDao.shoppingListsFlow.emit(listOf(entity))

            assertEquals(listOf(shoppingList()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `создает список для текущего пользователя`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID

        val result = repository.createShoppingList(name = LIST_NAME, iconName = ICON_NAME)

        assertEquals(CREATED_LIST_ID, result)
        assertEquals(
            ShoppingListEntity(
                name = LIST_NAME,
                iconName = ICON_NAME,
                ownerUserId = USER_ID,
            ),
            shoppingListDao.insertedLists.single(),
        )
    }

    @Test
    fun `обновляет имя списка только для текущего пользователя`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID

        repository.updateShoppingListName(shoppingListId = LIST_ID, name = RENAMED_LIST_NAME)

        assertEquals(
            FakeShoppingListDao.NameUpdate(
                shoppingListId = LIST_ID,
                name = RENAMED_LIST_NAME,
                ownerUserId = USER_ID,
            ),
            shoppingListDao.nameUpdates.single(),
        )
    }

    @Test
    fun `удаляет список только у текущего пользователя`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID

        repository.deleteShoppingList(LIST_ID)

        assertEquals(
            FakeShoppingListDao.ListRequest(shoppingListId = LIST_ID, ownerUserId = USER_ID),
            shoppingListDao.deletedLists.single(),
        )
    }

    private fun shoppingListEntity(): ShoppingListEntity {
        return ShoppingListEntity(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
            ownerUserId = USER_ID,
            sortType = SORT_TYPE,
        )
    }

    private fun shoppingList(): ShoppingList {
        return ShoppingList(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
            sortType = SORT_TYPE,
        )
    }

    private fun authSession(): AuthSession {
        return AuthSession(
            userId = USER_ID,
            email = EMAIL,
            accessToken = ACCESS_TOKEN,
            refreshToken = REFRESH_TOKEN,
        )
    }

    private companion object {
        const val USER_ID = 42L
        const val LIST_ID = 1L
        const val CREATED_LIST_ID = 100L
        const val LIST_NAME = "Продукты"
        const val RENAMED_LIST_NAME = "Дом"
        const val ICON_NAME = "cart"
        const val SORT_TYPE = "CUSTOM"
        const val EMAIL = "student@example.com"
        const val ACCESS_TOKEN = "access-token"
        const val REFRESH_TOKEN = "refresh-token"
    }
}

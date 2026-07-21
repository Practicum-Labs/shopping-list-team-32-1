package com.practicum.shoppinglist.data.repository

import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem
import com.practicum.shoppinglist.domain.usecase.auth.FakeAuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ShoppingItemRepositoryImplTest {

    private val shoppingItemDao = FakeShoppingItemDao()
    private val shoppingListDao = FakeShoppingListDao()
    private val authRepository = FakeAuthRepository()
    private val repository = ShoppingItemRepositoryImpl(
        shoppingItemDao = shoppingItemDao,
        shoppingListDao = shoppingListDao,
        authRepository = authRepository,
    )

    @Test
    fun `возвращает пустой список товаров если пользователь не авторизован`() = runTest {
        val result = repository.getItemsForListFlow(LIST_ID).first()

        assertEquals(emptyList<ShoppingItem>(), result)
    }

    @Test
    fun `получает товары текущего пользователя`() = runTest {
        authRepository.emitSession(authSession())
        shoppingItemDao.itemsForList = listOf(shoppingItemEntity())

        val result = repository.getItemsForListFlow(LIST_ID).first()

        assertEquals(listOf(shoppingItem()), result)
        assertEquals(
            FakeShoppingItemDao.ListRequest(listId = LIST_ID, ownerUserId = USER_ID),
            shoppingItemDao.requestedListIds.single(),
        )
    }

    @Test
    fun `добавляет товар если список принадлежит текущему пользователю`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID
        shoppingListDao.shoppingListById = shoppingListEntity()

        val result = repository.insertItem(shoppingItem())

        assertEquals(INSERTED_ITEM_ID, result)
        assertEquals(shoppingItemEntity(), shoppingItemDao.insertedItems.single())
    }

    @Test
    fun `не добавляет товар если список не принадлежит текущему пользователю`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID
        shoppingListDao.shoppingListById = null

        try {
            repository.insertItem(shoppingItem())
            fail("Ожидали SecurityException")
        } catch (exception: SecurityException) {
            assertEquals(
                "List $LIST_ID not found or access denied for user $USER_ID",
                exception.message,
            )
        }
    }

    @Test
    fun `удаляет все товары списка только у текущего пользователя`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID

        repository.deleteAllItems(LIST_ID)

        assertEquals(
            FakeShoppingItemDao.ListRequest(listId = LIST_ID, ownerUserId = USER_ID),
            shoppingItemDao.deletedListRequests.single(),
        )
    }

    @Test
    fun `очищает купленные товары только у текущего пользователя`() = runTest {
        authRepository.requiredCurrentUserId = USER_ID

        repository.clearBoughtItems(LIST_ID)

        assertEquals(
            FakeShoppingItemDao.ListRequest(listId = LIST_ID, ownerUserId = USER_ID),
            shoppingItemDao.deletedBoughtListRequests.single(),
        )
    }

    private fun shoppingItem(): ShoppingItem {
        return ShoppingItem(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            isBought = true,
            sortOrder = SORT_ORDER,
        )
    }

    private fun shoppingItemEntity(): ShoppingItemEntity {
        return ShoppingItemEntity(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            isBought = true,
            sortOrder = SORT_ORDER,
        )
    }

    private fun shoppingListEntity(): ShoppingListEntity {
        return ShoppingListEntity(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
            ownerUserId = USER_ID,
        )
    }

    private fun authSession() = com.practicum.shoppinglist.domain.model.AuthSession(
        userId = USER_ID,
        email = EMAIL,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
    )

    private companion object {
        const val USER_ID = 42L
        const val LIST_ID = 1L
        const val ITEM_ID = 10L
        const val INSERTED_ITEM_ID = 100L
        const val LIST_NAME = "Продукты"
        const val ICON_NAME = "cart"
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 1.0
        const val ITEM_UNIT = "л"
        const val SORT_ORDER = 3
        const val EMAIL = "student@example.com"
        const val ACCESS_TOKEN = "access-token"
        const val REFRESH_TOKEN = "refresh-token"
    }
}

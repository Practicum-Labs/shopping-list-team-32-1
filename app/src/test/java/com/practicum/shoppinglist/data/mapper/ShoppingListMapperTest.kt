package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.domain.model.ShoppingList
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingListMapperTest {

    @Test
    fun `маппит все поля entity списка в domain модель`() {
        val entity = ShoppingListEntity(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
            ownerUserId = OWNER_USER_ID,
            sortType = SORT_TYPE,
        )

        val result = entity.toDomain()

        assertEquals(
            ShoppingList(
                id = LIST_ID,
                name = LIST_NAME,
                iconName = ICON_NAME,
                sortType = SORT_TYPE,
            ),
            result,
        )
    }

    @Test
    fun `маппит список в entity с владельцем`() {
        val shoppingList = ShoppingList(
            id = LIST_ID,
            name = LIST_NAME,
            iconName = ICON_NAME,
            sortType = SORT_TYPE,
        )

        val result = shoppingList.toEntity(ownerUserId = OWNER_USER_ID)

        assertEquals(
            ShoppingListEntity(
                id = LIST_ID,
                name = LIST_NAME,
                iconName = ICON_NAME,
                ownerUserId = OWNER_USER_ID,
                sortType = SORT_TYPE,
            ),
            result,
        )
    }

    private companion object {
        const val LIST_ID = 7L
        const val LIST_NAME = "Продукты"
        const val ICON_NAME = "cart"
        const val OWNER_USER_ID = 42L
        const val SORT_TYPE = "ALPHABET"
    }
}

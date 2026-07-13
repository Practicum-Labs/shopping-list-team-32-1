package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingItemMapperTest {

    @Test
    fun `маппит все поля entity товара в domain модель`() {
        val entity = ShoppingItemEntity(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            isBought = true,
            sortOrder = SORT_ORDER,
        )

        val result = entity.toDomain()

        assertEquals(
            ShoppingItem(
                id = ITEM_ID,
                listId = LIST_ID,
                name = ITEM_NAME,
                quantity = ITEM_QUANTITY,
                unit = ITEM_UNIT,
                isBought = true,
                sortOrder = SORT_ORDER,
            ),
            result,
        )
    }

    @Test
    fun `маппит все поля domain модели товара в entity`() {
        val item = ShoppingItem(
            id = ITEM_ID,
            listId = LIST_ID,
            name = ITEM_NAME,
            quantity = ITEM_QUANTITY,
            unit = ITEM_UNIT,
            isBought = true,
            sortOrder = SORT_ORDER,
        )

        val result = item.toEntity()

        assertEquals(
            ShoppingItemEntity(
                id = ITEM_ID,
                listId = LIST_ID,
                name = ITEM_NAME,
                quantity = ITEM_QUANTITY,
                unit = ITEM_UNIT,
                isBought = true,
                sortOrder = SORT_ORDER,
            ),
            result,
        )
    }

    private companion object {
        const val ITEM_ID = 10L
        const val LIST_ID = 20L
        const val ITEM_NAME = "Молоко"
        const val ITEM_QUANTITY = 2.5
        const val ITEM_UNIT = "л"
        const val SORT_ORDER = 3
    }
}

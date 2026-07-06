package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.domain.model.ShoppingItem

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        listId = listId,
        name = name,
        quantity = quantity,
        unit = unit,
        isBought = isBought,
        sortOrder = sortOrder
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = id,
        listId = listId,
        name = name,
        quantity = quantity,
        unit = unit,
        isBought = isBought,
        sortOrder = sortOrder
    )
}

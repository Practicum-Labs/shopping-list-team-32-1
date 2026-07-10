package com.practicum.shoppinglist.data.mapper

import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.domain.model.ShoppingList

fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
        iconName = iconName,
    )
}

fun ShoppingList.toEntity(ownerUserId: Long): ShoppingListEntity {
    return ShoppingListEntity(
        id = id,
        name = name,
        iconName = iconName,
        ownerUserId = ownerUserId,
    )
}

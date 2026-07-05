package com.practicum.shoppinglist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        ShoppingItemEntity::class,
        ProductSuggestionEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}

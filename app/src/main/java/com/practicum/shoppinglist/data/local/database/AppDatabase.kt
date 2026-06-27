package com.practicum.shoppinglist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}

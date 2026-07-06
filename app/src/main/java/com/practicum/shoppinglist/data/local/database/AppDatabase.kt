package com.practicum.shoppinglist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `shopping_items` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`listId` INTEGER NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`quantity` REAL NOT NULL, " +
                        "`unit` TEXT NOT NULL, " +
                        "`isBought` INTEGER NOT NULL, " +
                        "`sortOrder` INTEGER NOT NULL" +
                        ")"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `product_suggestions` (" +
                        "`name` TEXT NOT NULL PRIMARY KEY" +
                        ")"
                )
            }
        }
    }
}

package com.practicum.shoppinglist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.shoppinglist.data.local.dao.ShoppingItemDao
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        ShoppingItemEntity::class,
        ProductSuggestionEntity::class,
    ],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemDao(): ShoppingItemDao

    companion object {
        val MIGRATION_1_2 = object : Migration(
            DATABASE_VERSION_1,
            DATABASE_VERSION_2,
        ) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE shopping_lists ADD COLUMN owner_user_id INTEGER NOT NULL DEFAULT 0",
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(
            DATABASE_VERSION_2,
            DATABASE_VERSION_3,
        ) {
            override fun migrate(db: SupportSQLiteDatabase) = Unit
        }

        val MIGRATION_3_4 = object : Migration(
            DATABASE_VERSION_3,
            DATABASE_VERSION_4,
        ) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `shopping_items` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `listId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `quantity` REAL NOT NULL,
                        `unit` TEXT NOT NULL,
                        `isBought` INTEGER NOT NULL,
                        `sortOrder` INTEGER NOT NULL,
                        FOREIGN KEY(`listId`) REFERENCES `shopping_lists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_shopping_items_listId` " +
                        "ON `shopping_items` (`listId`)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `product_suggestions` (" +
                        "`name` TEXT NOT NULL PRIMARY KEY" +
                        ")"
                )
                seedProductSuggestions(db)
            }
        }

        fun seedProductSuggestions(db: SupportSQLiteDatabase) {
            val defaults = listOf(
                "Кокосовое молоко",
                "Молоко",
                "Соевое молоко",
                "Сухое молоко",
                "Хлеб",
                "Яблоки",
                "Бананы",
                "Яйца",
                "Сыр",
                "Масло"
            )
            defaults.forEach { suggestion ->
                db.execSQL("INSERT OR IGNORE INTO product_suggestions (name) VALUES ('${suggestion}')")
            }
        }
    }
}

private const val DATABASE_VERSION_1 = 1
private const val DATABASE_VERSION_2 = 2
private const val DATABASE_VERSION_3 = 3
private const val DATABASE_VERSION_4 = 4

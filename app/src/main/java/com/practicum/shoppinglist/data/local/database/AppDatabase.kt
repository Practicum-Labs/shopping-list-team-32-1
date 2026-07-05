package com.practicum.shoppinglist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.practicum.shoppinglist.data.local.dao.ShoppingListDao
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

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
    }
}

private const val DATABASE_VERSION_1 = 1
private const val DATABASE_VERSION_2 = 2
private const val DATABASE_VERSION_3 = 3

package com.practicum.shoppinglist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists ORDER BY id DESC")
    fun observeShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE id = :shoppingListId LIMIT 1")
    suspend fun getShoppingListById(shoppingListId: Long): ShoppingListEntity?

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long

    @Query("UPDATE shopping_lists SET icon_name = :iconName WHERE id = :shoppingListId")
    suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
    )

    @Query("DELETE FROM shopping_lists")
    suspend fun deleteAllShoppingLists()

    @Query("DELETE FROM shopping_lists WHERE id = :shoppingListId")
    suspend fun deleteShoppingList(shoppingListId: Long)

    @Query("UPDATE shopping_lists SET name = :name WHERE id = :shoppingListId")
    suspend fun updateShoppingListName(
        shoppingListId: Long,
        name: String,
    )
}

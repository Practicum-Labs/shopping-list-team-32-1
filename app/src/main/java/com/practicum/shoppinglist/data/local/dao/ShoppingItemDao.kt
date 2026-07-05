package com.practicum.shoppinglist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.shoppinglist.data.local.entity.ProductSuggestionEntity
import com.practicum.shoppinglist.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY sortOrder ASC")
    fun getItemsForListFlow(listId: Long): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY sortOrder ASC")
    suspend fun getItemsForList(listId: Long): List<ShoppingItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Update
    suspend fun updateItems(items: List<ShoppingItemEntity>)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE listId = :listId")
    suspend fun deleteItemsForList(listId: Long)

    @Query("DELETE FROM shopping_items WHERE listId = :listId AND isBought = 1")
    suspend fun deleteBoughtItemsForList(listId: Long)

    @Query("SELECT * FROM product_suggestions WHERE name LIKE :query || '%' LIMIT 5")
    fun getSuggestionsFlow(query: String): Flow<List<ProductSuggestionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuggestion(suggestion: ProductSuggestionEntity)
}

package com.practicum.shoppinglist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.practicum.shoppinglist.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists WHERE owner_user_id = :ownerUserId ORDER BY id DESC")
    fun observeShoppingLists(ownerUserId: Long): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE id = :shoppingListId AND owner_user_id = :ownerUserId LIMIT 1")
    suspend fun getShoppingListById(shoppingListId: Long, ownerUserId: Long): ShoppingListEntity?

    @Query("SELECT * FROM shopping_lists WHERE id = :shoppingListId LIMIT 1")
    suspend fun getShoppingListById(shoppingListId: Long): ShoppingListEntity?

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long

    @Query(
        """
        UPDATE shopping_lists
        SET icon_name = :iconName
        WHERE id = :shoppingListId AND owner_user_id = :ownerUserId
        """,
    )
    suspend fun updateShoppingListIcon(
        shoppingListId: Long,
        iconName: String,
        ownerUserId: Long,
    )

    @Query("DELETE FROM shopping_lists WHERE owner_user_id = :ownerUserId")
    suspend fun deleteAllShoppingLists(ownerUserId: Long)

    @Query("DELETE FROM shopping_lists WHERE id = :shoppingListId AND owner_user_id = :ownerUserId")
    suspend fun deleteShoppingList(shoppingListId: Long, ownerUserId: Long)

    @Query(
        """
        UPDATE shopping_lists
        SET name = :name
        WHERE id = :shoppingListId AND owner_user_id = :ownerUserId
        """,
    )
    suspend fun updateShoppingListName(
        shoppingListId: Long,
        name: String,
        ownerUserId: Long,
    )
}

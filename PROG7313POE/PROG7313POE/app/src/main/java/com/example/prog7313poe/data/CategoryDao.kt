package com.example.prog7313poe.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.prog7313poe.model.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    fun getCategories(userId: String): LiveData<List<Category>>
}
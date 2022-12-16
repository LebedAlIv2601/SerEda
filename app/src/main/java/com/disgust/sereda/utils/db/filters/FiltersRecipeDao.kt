package com.disgust.sereda.utils.db.filters

import androidx.room.*

@Dao
interface FiltersRecipeDao {

    @Query("SELECT * FROM filters_recipes")
    fun getFiltersRecipe(): List<FilterRecipeDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilterRecipe(filter: FilterRecipeDBModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFiltersRecipe(filters: List<FilterRecipeDBModel>)

    @Delete
    fun deleteFilterRecipe(filter: FilterRecipeDBModel)

    @Query("DELETE FROM filters_recipes")
    fun deleteAllFiltersRecipe()

    @Transaction
    fun updateFilterRecipeByIngredient(list: List<FilterRecipeDBModel>) {
        deleteAllFiltersRecipe()
        insertFiltersRecipe(list)
    }

}
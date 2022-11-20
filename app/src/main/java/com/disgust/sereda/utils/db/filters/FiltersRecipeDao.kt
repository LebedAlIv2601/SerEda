package com.disgust.sereda.utils.db.filters

import androidx.room.*

@Dao
interface FiltersRecipeDao {

    @Query("SELECT * FROM filters_recipes")
    fun getFiltersRecipe(): List<FilterRecipeDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilterRecipe(filter: FilterRecipeDBModel)

    @Delete
    fun deleteFilterRecipe(filter: FilterRecipeDBModel)

    @Query("DELETE FROM filters_recipes")
    fun deleteAllFiltersRecipe()

    @Transaction
    fun getFilterRecipeByIngredients(): List<FilterRecipeDBModel> {
        val list = getFiltersRecipe()
        deleteAllFiltersRecipe()
        return list
    }

}
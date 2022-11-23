package com.disgust.sereda.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDBModel
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDao
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel
import com.disgust.sereda.utils.db.filters.FiltersRecipeDao

@Database(entities = [FavoriteRecipeDBModel::class, FilterRecipeDBModel::class], version = 2)
abstract class SerEdaDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun filtersRecipeDao(): FiltersRecipeDao
}
package com.disgust.sereda.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDBModel
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDao

@Database(entities = [FavoriteRecipeDBModel::class], version = 1)
abstract class SerEdaDatabase : RoomDatabase() {
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
}
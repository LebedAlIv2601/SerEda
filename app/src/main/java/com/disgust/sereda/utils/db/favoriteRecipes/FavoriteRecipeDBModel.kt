package com.disgust.sereda.utils.db.favoriteRecipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val image: String?
)

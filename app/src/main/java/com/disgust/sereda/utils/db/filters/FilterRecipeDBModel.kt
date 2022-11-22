package com.disgust.sereda.utils.db.filters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters_recipes")
data class FilterRecipeDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val image: String?,
    val isInclude: Boolean
)
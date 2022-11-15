package com.disgust.sereda.recipe.screens.search.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeListResponse(
    @Json
    val results: List<RecipeItem>
)

@JsonClass(generateAdapter = true)
data class RecipeRandomListResponse(
    @Json
    val recipes: List<RecipeItem>
)

@JsonClass(generateAdapter = true)
data class RecipeItem(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val name: String,

    @Json(name = "image")
    val image: String,
)
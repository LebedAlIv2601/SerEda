package com.disgust.sereda.ingredients.screens.search.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientsListResponse(
    @Json
    val results: List<IngredientItem>
)

@JsonClass(generateAdapter = true)
data class IngredientItem(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "image")
    val image: String
)

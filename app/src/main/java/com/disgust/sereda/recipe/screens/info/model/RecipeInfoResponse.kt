package com.disgust.sereda.recipe.screens.info.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeInfoResponse(
    @Json(name = "title")
    val name: String,

    @Json(name = "image")
    val image: String,

    @Json(name = "readyInMinutes")
    val time: Int,

    @Json(name = "servings")
    val servings: Int,

    @Json(name = "summary")
    val cooking: String, //выводить надо пошагово

    @Json(name = "servings")
    val servings: Int,
)

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
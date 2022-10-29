package com.disgust.sereda.recipe.screens.info.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeInfoResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val name: String,

    @Json(name = "image")
    val image: String,

    @Json(name = "readyInMinutes")
    val time: Int,

    @Json(name = "servings")
    val servings: Int,

    @Json(name = "analyzedInstructions")
    val instructions: List<Instruction>?,

    @Json(name = "diets")
    val diets: List<String>?,

    @Json(name = "extendedIngredients")
    val ingredients: List<Ingredient>?,

    @Json(name = "nutrition")
    val nutrition: Nutrition?,
)

@JsonClass(generateAdapter = true)
data class Nutrition(
    @Json(name = "nutrients")
    val nutrients: List<Nutrient>?,
)

@JsonClass(generateAdapter = true)
data class Nutrient(
    @Json(name = "name")
    val name: String,

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "unit")
    val unit: String,

    @Json(name = "percentOfDailyNeeds")
    val percent: Double,
)

@JsonClass(generateAdapter = true)
data class Ingredient(
    @Json(name = "name")
    val name: String,

    @Json(name = "measures")
    val measures: Measures,

    @Json(name = "image")
    val imageName: String,
)

@JsonClass(generateAdapter = true)
data class Measures(
    @Json(name = "us")
    val us: Quantity,

    @Json(name = "metric")
    val metric: Quantity,
)

@JsonClass(generateAdapter = true)
data class Quantity(
    @Json(name = "amount")
    val amount: Double,

    @Json(name = "unitShort")
    val unitShort: String,

    @Json(name = "unitLong")
    val unitLong: String,
)

@JsonClass(generateAdapter = true)
data class Instruction(
    @Json(name = "steps")
    val step: List<StepRecipe>,
)

@JsonClass(generateAdapter = true)
data class StepRecipe(
    @Json(name = "number")
    val number: Int,

    @Json(name = "step")
    val step: String,

    @Json(name = "equipment")
    val equipment: List<Equipment>,
)

@JsonClass(generateAdapter = true)
data class Equipment(
    @Json(name = "name")
    val name: String
)
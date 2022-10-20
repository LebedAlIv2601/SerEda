package com.disgust.sereda.ingredients.screens.info.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientInfoResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "image")
    val image: String,

    @Json(name = "nutrition")
    val nutrition: IngredientNutritionResponse
)

@JsonClass(generateAdapter = true)
data class IngredientNutritionResponse(
    @Json(name = "nutrients")
    val nutrients: List<NutritionItem>,

    @Json(name = "caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown
)

@JsonClass(generateAdapter = true)
data class CaloricBreakdown(
    @Json(name = "percentProtein")
    val percentProtein: Float,

    @Json(name = "percentFat")
    val percentFat: Float,

    @Json(name = "percentCarbs")
    val percentCarbs: Float
)

@JsonClass(generateAdapter = true)
data class NutritionItem(
    @Json(name = "name")
    val name: String = "Not Find",

    @Json(name = "amount")
    val amount: Float = 0.0f,

    @Json(name = "unit")
    val unit: String = ""
)
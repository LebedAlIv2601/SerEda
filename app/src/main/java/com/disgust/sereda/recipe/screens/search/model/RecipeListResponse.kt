package com.disgust.sereda.recipe.screens.search.model

import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeListResponse(
    @Json
    val results: List<RecipeItemResponse>
)

@JsonClass(generateAdapter = true)
data class RecipeItemResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val name: String,

    @Json(name = "image")
    val image: String?
)

data class RecipeItem(
    val id: Int,

    val name: String,

    val image: String?,

    val favoriteState: RecipeFavoriteState = RecipeFavoriteState.NOT_FAVORITE
)
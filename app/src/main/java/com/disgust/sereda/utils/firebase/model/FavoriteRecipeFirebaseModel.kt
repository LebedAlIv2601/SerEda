package com.disgust.sereda.utils.firebase.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteRecipeFirebaseModel(
    val id: String,
    val name: String,
    val image: String? = null
) {
    fun toMap() = mapOf<String, Any?>(
        "id" to id,
        "name" to name,
        "image" to image
    )
}

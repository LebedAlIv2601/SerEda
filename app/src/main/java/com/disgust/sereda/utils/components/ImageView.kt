package com.disgust.sereda.utils.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.disgust.sereda.R

@Composable
fun ImageIngredientView(url: String) {
    ImageView(url = "https://spoonacular.com/cdn/ingredients_500x500/$url")
}

@Composable
fun ImageRecipeView(url: String) {
    ImageView(url = url)
}

@Composable
private fun ImageView(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.ic_load_test_24),
        error = painterResource(id = R.drawable.ic_error_test_24)
    )
}
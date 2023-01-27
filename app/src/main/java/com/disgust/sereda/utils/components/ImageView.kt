package com.disgust.sereda.utils.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.disgust.sereda.R

@Composable
fun ImageIngredientView(
    url: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    ImageView(
        url = "https://spoonacular.com/cdn/ingredients_500x500/$url",
        modifier = modifier,
        contentScale = contentScale
    )
}

@Composable
fun ImageRecipeView(
    id: Int,
    modifier: Modifier = Modifier,
    size: RecipeImageSize = RecipeImageSize.SIZE_312_231,
    contentScale: ContentScale = ContentScale.Crop
) {
    ImageView(
        url = "https://spoonacular.com/recipeImages/$id-${size.value}.jpg",
        modifier = modifier,
        contentScale = contentScale
    )
}

enum class RecipeImageSize(val value: String) {
    SIZE_90_90("90x90"),
    SIZE_240_150("240x150"),
    SIZE_312_150("312x150"),
    SIZE_312_231("312x231"),
    SIZE_480_360("480x360"),
    SIZE_556_370("556x370"),
    SIZE_636_393("636x393")
}

@Composable
private fun ImageView(
    url: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.ic_load_test_24),
        error = painterResource(id = R.drawable.ic_error_test_24),
        modifier = modifier,
        contentScale = contentScale
    )
}
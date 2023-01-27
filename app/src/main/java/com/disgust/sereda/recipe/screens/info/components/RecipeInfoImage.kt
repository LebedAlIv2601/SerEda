package com.disgust.sereda.recipe.screens.info.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.disgust.sereda.utils.components.ImageRecipeView
import com.disgust.sereda.utils.components.RecipeImageSize

@Composable
fun RecipeInfoImage(id: Int) {
    ImageRecipeView(
        id = id,
        size = RecipeImageSize.SIZE_636_393,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Blue, CircleShape)
    )
}
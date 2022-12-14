package com.disgust.sereda.recipe.screens.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState

@Composable
fun RecipeListItem(
    recipe: RecipeItem,
    onItemClick: (RecipeItem) -> Unit,
    onAddToFavoriteButtonClick: (RecipeItem) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onItemClick(recipe) }
            .padding(bottom = 16.dp)
            .border(width = 2.dp, color = Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = recipe.name, modifier =
            Modifier
                .weight(3f)
                .padding(start = 16.dp)
        )
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopEnd
        ) {
            when (recipe.favoriteState) {
                RecipeFavoriteState.NOT_FAVORITE -> {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable { onAddToFavoriteButtonClick(recipe) }
                    )
                }
                RecipeFavoriteState.FAVORITE -> {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable { onAddToFavoriteButtonClick(recipe) }
                    )
                }
            }

        }
    }
}
package com.disgust.sereda.recipe.screens.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.components.ImageRecipeView
import com.disgust.sereda.utils.components.RecipeImageSize

@Composable
fun RecipeListItem(
    recipe: RecipeItem,
    onItemClick: (RecipeItem) -> Unit,
    onAddToFavoriteButtonClick: (RecipeItem) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(recipe) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImageRecipeView(
                id = recipe.id,
                size = RecipeImageSize.SIZE_480_360,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Blue, CircleShape)
            )
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
}

@Preview
@Composable
fun RecipeItemPreview() {
    RecipeListItem(
        recipe = RecipeItem(
            id = 0,
            name = "Apple BadAss Pie",
            image = "https://spoonacular.com/recipeImages/716429-312x231.jpg",
            favoriteState = RecipeFavoriteState.NOT_FAVORITE
        ),
        onItemClick = {},
        onAddToFavoriteButtonClick = {})
}
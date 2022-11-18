package com.disgust.sereda.recipe.screens.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.commonModel.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonViews.ImageIngredientView
import com.disgust.sereda.utils.commonViews.ImageRecipeView

@Composable
fun RecipeInfoScreen(
    navController: NavHostController,
    vm: RecipeInfoViewModel = hiltViewModel(),
    recipeId: Int,
    favoriteState: Int
) {
    val recipeInfoState = vm.recipeInfoState.collectAsState()

    DoOnInit {
        vm.onUIEvent(RecipeInfoUIEvent.StartScreen(recipeId, favoriteState))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                if (recipeInfoState.value is RecipeInfoState.Success)
                    (recipeInfoState.value as RecipeInfoState.Success).data.image?.let {
                        ImageRecipeView(
                            url = it
                        )
                    }
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                if (recipeInfoState.value is RecipeInfoState.Success)
                    (recipeInfoState.value as RecipeInfoState.Success).data.ingredients?.get(0)?.imageName?.let {
                        ImageIngredientView(
                            url = it
                        )
                    }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (recipeInfoState.value) {
                    is RecipeInfoState.Loading -> {
                        Text(
                            text = "Loading $recipeId"
                        )
                    }
                    is RecipeInfoState.Success -> {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when ((recipeInfoState.value as RecipeInfoState.Success).data.favoriteState) {
                                RecipeFavoriteState.NOT_FAVORITE -> {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(100.dp)
                                            .clickable { vm.onUIEvent(RecipeInfoUIEvent.ButtonAddToFavoriteClick) }
                                    )
                                }
                                RecipeFavoriteState.FAVORITE -> {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(100.dp)
                                            .clickable { vm.onUIEvent(RecipeInfoUIEvent.ButtonAddToFavoriteClick) }
                                    )
                                }
                            }

                        }
                        Text(
                            text =
                            (recipeInfoState.value as RecipeInfoState.Success).data.toString()
                        )
                    }
                    is RecipeInfoState.Error -> (recipeInfoState.value as RecipeInfoState.Error).error.toString()
                }
            }
        }
    }


}
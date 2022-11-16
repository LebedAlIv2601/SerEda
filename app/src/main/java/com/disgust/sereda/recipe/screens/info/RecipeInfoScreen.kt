package com.disgust.sereda.recipe.screens.info

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonViews.ImageIngredientView
import com.disgust.sereda.utils.commonViews.ImageRecipeView

@Composable
fun RecipeInfoScreen(
    navController: NavHostController,
    vm: RecipeInfoViewModel = hiltViewModel(),
    recipeId: Int
) {
    val recipeInfoState = vm.recipeInfoState.collectAsState()

    DoOnInit {
        vm.onUIEvent(RecipeInfoUIEvent.StartScreen(recipeId))
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
                    .height(300.dp),
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
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (recipeInfoState.value is RecipeInfoState.Success)
                    (recipeInfoState.value as RecipeInfoState.Success).data.ingredients?.get(0)?.imageName?.let {
                        ImageIngredientView(
                            url = it
                        )
                    }
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text =
                    when (recipeInfoState.value) {
                        is RecipeInfoState.Loading -> "Loading $recipeId"
                        is RecipeInfoState.Success -> (recipeInfoState.value as RecipeInfoState.Success).data.toString()
                        is RecipeInfoState.Error -> (recipeInfoState.value as RecipeInfoState.Error).error.toString()
                    }
                )
            }
        }
    }


}
package com.disgust.sereda.recipe.screens.info

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit

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

    Text(
        text =
        when (recipeInfoState.value) {
            is RecipeInfoState.Loading -> "Loading $recipeId"
            is RecipeInfoState.Success -> (recipeInfoState.value as RecipeInfoState.Success).data.toString()
            is RecipeInfoState.Error -> (recipeInfoState.value as RecipeInfoState.Error).error.toString()
        }
    )
}
package com.disgust.sereda.ingredients.screens.info

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoState
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoUIEvent
import com.disgust.sereda.utils.DoOnInit

@Composable
fun IngredientInfoScreen(
    vm: IngredientInfoViewModel,
    ingredientId: Int
) {
    val ingredientInfoState = vm.ingredientInfoState.collectAsState()

    DoOnInit {
        vm.onUIEvent(IngredientInfoUIEvent.StartScreen(ingredientId))
    }

    Text(
        text =
        when (ingredientInfoState.value) {
            is IngredientInfoState.Loading -> "Loading $ingredientId"
            is IngredientInfoState.Success -> (ingredientInfoState.value as IngredientInfoState.Success).data.toString()
            is IngredientInfoState.Error -> (ingredientInfoState.value as IngredientInfoState.Error).error.toString()
        }
    )
}
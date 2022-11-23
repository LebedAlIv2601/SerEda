package com.disgust.sereda.ingredients.screens.info

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoState
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoUIEvent
import com.disgust.sereda.utils.DoOnInit

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun IngredientInfoScreen(
    vm: IngredientInfoViewModel,
    ingredientId: Int,
    ingredientName: String
) {
    val ingredientInfoState = vm.ingredientInfoState.collectAsState()
    val enabledButtonFilters = vm.enabledButtonFilters.collectAsState()

    DoOnInit {
        vm.onUIEvent(IngredientInfoUIEvent.StartScreen(ingredientId, ingredientName))
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
            IconButton(
                onClick = {
                    vm.onUIEvent(
                        IngredientInfoUIEvent.IngredientAddButtonClick
                    )
                },
                enabled = enabledButtonFilters.value
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Включить в рецепт"
                )
            }

            IconButton(
                onClick = {
                    vm.onUIEvent(
                        IngredientInfoUIEvent.IngredientExcludeButtonClick
                    )
                },
                enabled = enabledButtonFilters.value
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Исключить из рецепта"
                )
            }
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


}
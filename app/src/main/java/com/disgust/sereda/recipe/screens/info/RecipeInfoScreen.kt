package com.disgust.sereda.recipe.screens.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.components.ImageIngredientView
import com.disgust.sereda.utils.components.ImageRecipeView

@Composable
fun RecipeInfoScreen(
    vm: RecipeInfoViewModel,
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
                        val data = (recipeInfoState.value as RecipeInfoState.Success).data
                        Text(
                            text = "имя ${data.name}\nкалории ${data.calories}\nвремя " +
                                    "${data.time}\nдиеты ${data.diets}\nингридиенты " +
                                    "${data.ingredients}",
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                    is RecipeInfoState.Error -> (recipeInfoState.value as RecipeInfoState.Error).error.toString()
                }
            }
        }
    }


}
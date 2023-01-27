package com.disgust.sereda.recipe.screens.info

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.recipe.screens.info.components.*
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.commonModel.UserNotAuthDialogState

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun RecipeInfoScreen(
    vm: RecipeInfoViewModel,
    recipeId: Int,
    favoriteState: Int
) {
    val recipeInfoState = vm.recipeInfoState.collectAsState()
    val userNotAuthDialogState = vm.userNotAuthDialogState.collectAsState()

    DoOnInit {
        vm.onUIEvent(RecipeInfoUIEvent.StartScreen(recipeId, favoriteState))
    }

    if (userNotAuthDialogState.value == UserNotAuthDialogState.SHOWN) {
        AlertDialog(
            onDismissRequest = {
                vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogDismiss(false))
            },
            title = { Text(text = "You are not authorized") },
            text = { Text(text = "You need to authorize for adding recipes to favorite") },
            confirmButton = {
                Button(onClick = { vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogDismiss(false)) }) {
                    Text(text = "Authorize")
                }
            },
            dismissButton = {
                Button(onClick = { vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogDismiss(false)) }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        recipeInfoState.value.doAsStateIfPossible<RecipeInfoState.Success> { recipeInfo ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RecipeInfoImage(id = recipeInfo.data.id)
                Text(
                    text = recipeInfo.data.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "${recipeInfo.data.calories?.amount} ${recipeInfo.data.calories?.unit}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            RecipeIngredientsRow(
                list = recipeInfo.data.ingredients ?: listOf(),
                onIngredientClick = {
                    vm.onUIEvent(
                        RecipeInfoUIEvent.IngredientItemClick(
                            it.id,
                            it.name
                        )
                    )
                }
            )
            NutritionPieChart(
                listOf(
                    ChartSlice(
                        color = Color.Red,
                        amount = recipeInfo.data.carbohydrates?.amount?.toFloat() ?: 0f,
                        title = recipeInfo.data.carbohydrates?.name ?: "",
                        unit = recipeInfo.data.carbohydrates?.unit
                    ),
                    ChartSlice(
                        color = Color.Green,
                        amount = recipeInfo.data.protein?.amount?.toFloat() ?: 0f,
                        title = recipeInfo.data.protein?.name ?: "",
                        unit = recipeInfo.data.protein?.unit
                    ),
                    ChartSlice(
                        color = Color.Yellow,
                        amount = recipeInfo.data.fat?.amount?.toFloat() ?: 0f,
                        title = recipeInfo.data.fat?.name ?: "",
                        unit = recipeInfo.data.fat?.unit
                    )
                )
            )
            NutritionGrid(nutrition = recipeInfo.data.nutrition ?: emptyList())
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val recipeInfoStateValue = recipeInfoState.value) {
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
                        when (recipeInfoStateValue.data.favoriteState) {
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
                    val data = recipeInfoStateValue.data
                    Text(
                        text = "имя ${data.name}\nкалории ${data.calories}\nвремя " +
                                "${data.time}\nдиеты ${data.diets}\nингридиенты " +
                                "${data.ingredients}"
                    )
                }
                is RecipeInfoState.Error -> recipeInfoStateValue.error.toString()
            }
        }
    }


}
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
import androidx.compose.ui.unit.dp
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.commonModel.UserNotAuthDialogState
import com.disgust.sereda.utils.components.ImageIngredientView
import com.disgust.sereda.utils.components.ImageRecipeView

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
                vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogDismiss)
            },
            title = { Text(text = "You are not authorized") },
            text = { Text(text = "You need to authorize for adding recipes to favorite") },
            confirmButton = {
                Button(onClick = { vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogConfirmButtonClick) }) {
                    Text(text = "Authorize")
                }
            },
            dismissButton = {
                Button(onClick = { vm.onUIEvent(RecipeInfoUIEvent.UserNotAuthDialogDismiss) }) {
                    Text(text = "Cancel")
                }
            }
        )
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
                            text = "?????? ${data.name}\n?????????????? ${data.calories}\n?????????? " +
                                    "${data.time}\n?????????? ${data.diets}\n?????????????????????? " +
                                    "${data.ingredients}",
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                    is RecipeInfoState.Error -> recipeInfoStateValue.error.toString()
                }
            }
        }
    }


}
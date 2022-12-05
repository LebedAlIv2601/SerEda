package com.disgust.sereda.favorite.favoriteList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.disgust.sereda.favorite.favoriteList.interaction.FavoriteRecipesListState
import com.disgust.sereda.favorite.favoriteList.interaction.FavoriteUIEvent
import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.utils.DoOnInit

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun FavoriteScreen(
    vm: FavoriteViewModel
) {

    val favoriteRecipesState = vm.recipesListState.collectAsState()

    DoOnInit {
        vm.onUIEvent(FavoriteUIEvent.StartScreen)
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        when (val favoriteList = favoriteRecipesState.value) {
            is FavoriteRecipesListState.Loading -> {
                CircularProgressIndicator()
            }
            is FavoriteRecipesListState.Updated -> {
                Button({ vm.onUIEvent(FavoriteUIEvent.UpdateButtonClick) }) {
                    Text(text = "Update list")
                }
                RecipesList(
                    recipes = favoriteList.data,
                    onItemClick = { vm.onUIEvent(FavoriteUIEvent.FavoriteRecipesListItemClick(it)) },
                    deleteFromFavoriteButtonClick = {
                        vm.onUIEvent(
                            FavoriteUIEvent.DeleteFromFavoriteButtonClick(
                                it
                            )
                        )
                    }
                )
            }
            is FavoriteRecipesListState.Updating -> {
                CircularProgressIndicator()
                RecipesList(
                    recipes = favoriteList.data,
                    onItemClick = { vm.onUIEvent(FavoriteUIEvent.FavoriteRecipesListItemClick(it)) },
                    deleteFromFavoriteButtonClick = {
                        vm.onUIEvent(
                            FavoriteUIEvent.DeleteFromFavoriteButtonClick(
                                it
                            )
                        )
                    }
                )
            }
            is FavoriteRecipesListState.NotUpdated -> {
                Button({ vm.onUIEvent(FavoriteUIEvent.UpdateButtonClick) }) {
                    Text(text = "Update list")
                }
                Text(text = "Data can be old. Please, update")
                RecipesList(
                    recipes = favoriteList.data,
                    onItemClick = { vm.onUIEvent(FavoriteUIEvent.FavoriteRecipesListItemClick(it)) },
                    deleteFromFavoriteButtonClick = {
                        vm.onUIEvent(
                            FavoriteUIEvent.DeleteFromFavoriteButtonClick(
                                it
                            )
                        )
                    }
                )
            }
            is FavoriteRecipesListState.Error -> {
                Text(text = favoriteList.exception.toString())
            }

            is FavoriteRecipesListState.NotAuth -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "You need to authorize to see favorite recipes")
                    Button(onClick = {
                        vm.onUIEvent(FavoriteUIEvent.ButtonAuthClick)
                    }) {
                        Text(text = "Authorize")
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun RecipesList(
    recipes: List<FavoriteRecipe>,
    onItemClick: (FavoriteRecipe) -> Unit,
    deleteFromFavoriteButtonClick: (FavoriteRecipe) -> Unit
) {
    LazyColumn {
        items(recipes) { recipe ->
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
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable { deleteFromFavoriteButtonClick(recipe) }
                    )
                }
            }
        }
    }
}
package com.disgust.sereda.recipe.screens.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.commonModel.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonViews.SearchView

@ExperimentalComposeUiApi
@Composable
fun SearchRecipeScreen(
    navController: NavHostController,
    vm: SearchRecipeViewModel = hiltViewModel()
) {
    val recipesState = vm.recipesListState.collectAsState()
    val inputText = vm.inputText.collectAsState()
    val showKeyboard = vm.showKeyboard.collectAsState()

    DoOnInit {
        vm.onUIEvent(RecipesListUIEvent.StartScreen)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SearchView(
            value = inputText.value,
            onSearch = {
                vm.onUIEvent(RecipesListUIEvent.SearchClick(inputText.value))
            },
            onValueChange = {
                vm.onUIEvent(RecipesListUIEvent.InputTextChange(it))
            },
            showKeyboardValue = showKeyboard.value,
            setShowKeyboard = {
                vm.onUIEvent(RecipesListUIEvent.KeyboardInitShow)
            }
        )

        when (recipesState.value) {
            is RecipesListState.Loading -> Text("Loading")
            is RecipesListState.Success ->
                RecipesList(
                    recipes = (recipesState.value as RecipesListState.Success).data,
                    onItemClick = {
                        vm.onUIEvent(
                            RecipesListUIEvent.ListItemClick(
                                navController = navController,
                                item = it
                            )
                        )
                    },
                    onAddToFavoriteButtonClick = {
                        vm.onUIEvent(
                            RecipesListUIEvent.ListItemButtonAddToFavoriteClick(it)
                        )
                    }
                )
            is RecipesListState.Error -> Text("Error")
            else -> Text("")
        }

    }
}

@Composable
fun RecipesList(
    recipes: List<RecipeItem>,
    onItemClick: (RecipeItem) -> Unit,
    onAddToFavoriteButtonClick: (RecipeItem) -> Unit
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
}
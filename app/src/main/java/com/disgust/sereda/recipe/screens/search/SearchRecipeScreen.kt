package com.disgust.sereda.recipe.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.components.SearchView
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchRecipeScreen(
    vm: SearchRecipeViewModel
) {
    val recipesState = vm.recipesListState.collectAsState()
    val inputText = vm.inputText.collectAsState()
    val showKeyboard = vm.showKeyboard.collectAsState()
    val hideKeyboard = vm.hideKeyboard.collectAsState()

    val state = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    val ingredientsList = vm.ingredientsList.collectAsState()
    val dietsList = vm.dietList.collectAsState()
    val intolerancesList = vm.intolerancesList.collectAsState()
    val maxReadyTime = vm.maxReadyTime.collectAsState()
    val minCalories = vm.minCalories.collectAsState()
    val maxCalories = vm.maxCalories.collectAsState()

    DoOnInit {
        vm.onUIEvent(RecipesListUIEvent.StartScreen)
    }

    val topPanelFilter = @Composable {
        TopPanelFilter(
            onSearchIngredient = { vm.onUIEvent(RecipesListUIEvent.FiltersSearchIngredientButtonClick) },
            onClose = { scope.launch { state.hide() } },
            onDeleteAll = { vm.onUIEvent(RecipesListUIEvent.FiltersDeleteAllIngredients) })
    }

    val ingredientsListFilter = @Composable {
        ListIngredientFilter(
            list = ingredientsList.value,
            onDeleteItem = { vm.onUIEvent(RecipesListUIEvent.FiltersDeleteIngredient(it)) })
    }

    val dietsChips = @Composable {
        ChipsFilter(
            selectedChips = dietsList.value,
            setChipState = { diet, isAdd ->
                vm.onUIEvent(RecipesListUIEvent.FiltersSetDiet(diet, isAdd))
            })
    }

    val intolerancesChips = @Composable {
        ChipsFilter(
            selectedChips = intolerancesList.value,
            setChipState = { intolerance, isAdd ->
                vm.onUIEvent(RecipesListUIEvent.FiltersSetIntolerance(intolerance, isAdd))
            })
    }

    val maxReadyTimeFilter = @Composable {
        SingleInputFilter(
            label = "ReadyTime",
            value = maxReadyTime.value,
            onValueChange = { vm.onUIEvent(RecipesListUIEvent.FiltersInputReadyTimeChange(it)) })
    }

    val minMaxCaloriesInputFiler = @Composable {
        MinMaxInputFiler(
            labelMin = "Min Calories",
            labelMax = "Max Calories",
            valueMin = minCalories.value,
            valueMax = maxCalories.value,
            onValueChangeMin = { vm.onUIEvent(RecipesListUIEvent.FiltersInputMinCaloriesChange(it)) },
            onValueChangeMax = { vm.onUIEvent(RecipesListUIEvent.FiltersInputMaxCaloriesChange(it)) }
        )
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = MaterialTheme.shapes.small,
        sheetContent = {
            FiltersView(
                topPanelFilter, maxReadyTimeFilter, dietsChips, intolerancesChips,
                ingredientsListFilter, minMaxCaloriesInputFiler
            ) {
                scope.launch { state.hide() }
                vm.onUIEvent(RecipesListUIEvent.FiltersApplyButtonClick(inputText.value))
            }
        }) {

        Column() {
            Row {

                SearchView(
                    value = inputText.value,
                    onSearch = {
                        vm.onUIEvent(RecipesListUIEvent.SearchClick(inputText.value))
                    },
                    onValueChange = {
                        vm.onUIEvent(RecipesListUIEvent.InputTextChange(it))
                    },
                    showKeyboardValue = showKeyboard.value,
                    hideKeyboardValue = hideKeyboard.value,
                    setShowKeyboard = {
                        vm.onUIEvent(RecipesListUIEvent.KeyboardInitShow)
                    },
                    setHideKeyboard = {
                        vm.onUIEvent(RecipesListUIEvent.KeyboardSetHide)
                    })

                IconButton(
                    onClick = {
                        vm.onUIEvent(RecipesListUIEvent.FiltersOpenButtonClick)
                        scope.launch { state.animateTo(ModalBottomSheetValue.Expanded) }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Filters"
                    )
                }

                IconButton(
                    onClick = {
                        vm.onUIEvent(RecipesListUIEvent.ProfileButtonClick)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Профиль"
                    )
                }

                IconButton(
                    onClick = {
                        vm.onUIEvent(RecipesListUIEvent.FavoriteListButtonClick)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Избранное"
                    )
                }

            }

            when (val recipesValue = recipesState.value) {
                is RecipesListState.Loading -> Text("Loading")
                is RecipesListState.Success ->
                    RecipesList(
                        recipes = recipesValue.data,
                        onItemClick = {
                            vm.onUIEvent(
                                RecipesListUIEvent.ListItemClick(
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
                is RecipesListState.Error -> Text(recipesValue.exception.toString())
                else -> Text("")
            }
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

package com.disgust.sereda.recipe.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.disgust.sereda.recipe.screens.search.components.RecipeListItem
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.utils.Constants
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.UserNotAuthDialogState
import com.disgust.sereda.utils.components.PagingList
import com.disgust.sereda.utils.components.PagingState
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
    val userNotAuthDialogState = vm.userNotAuthDialogState.collectAsState()

    val state = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    val filtersRecipeChanged = vm.filtersRecipeChanged.collectAsState()

    val pagingState = remember { mutableStateOf<PagingState>(PagingState.Waiting) }

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
            list = filtersRecipeChanged.value.ingredientsList ?: listOf(),
            onDeleteItem = { vm.onUIEvent(RecipesListUIEvent.FiltersDeleteIngredient(it)) })
    }

    val dietsChips = @Composable {
        ChipsFilter(
            selectedChips = filtersRecipeChanged.value.dietsList ?: listOf(),
            setChipState = { diet, isAdd ->
                vm.onUIEvent(RecipesListUIEvent.FiltersSetDiet(diet, isAdd))
            })
    }

    val intolerancesChips = @Composable {
        ChipsFilter(
            selectedChips = filtersRecipeChanged.value.intolerancesList ?: listOf(),
            setChipState = { intolerance, isAdd ->
                vm.onUIEvent(RecipesListUIEvent.FiltersSetIntolerance(intolerance, isAdd))
            })
    }

    val maxReadyTimeFilter = @Composable {
        SingleInputFilter(
            label = "ReadyTime",
            value = filtersRecipeChanged.value.maxReadyTime,
            onValueChange = { vm.onUIEvent(RecipesListUIEvent.FiltersInputReadyTimeChange(it)) })
    }

    val minMaxCaloriesInputFilter = @Composable {
        MinMaxInputFilter(
            labelMin = "Min Calories",
            labelMax = "Max Calories",
            valueMin = filtersRecipeChanged.value.minCalories,
            valueMax = filtersRecipeChanged.value.maxCalories,
            onValueChangeMin = { vm.onUIEvent(RecipesListUIEvent.FiltersInputMinCaloriesChange(it)) },
            onValueChangeMax = { vm.onUIEvent(RecipesListUIEvent.FiltersInputMaxCaloriesChange(it)) }
        )
    }

    if (userNotAuthDialogState.value == UserNotAuthDialogState.SHOWN) {
        AlertDialog(
            onDismissRequest = {
                vm.onUIEvent(RecipesListUIEvent.UserNotAuthDialogDismiss)
            },
            title = { Text(text = "You are not authorized") },
            text = { Text(text = "You need to authorize for adding recipes to favorite") },
            confirmButton = {
                Button(onClick = { vm.onUIEvent(RecipesListUIEvent.UserNotAuthDialogConfirmButtonClick) }) {
                    Text(text = "Authorize")
                }
            },
            dismissButton = {
                Button(onClick = { vm.onUIEvent(RecipesListUIEvent.UserNotAuthDialogDismiss) }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = MaterialTheme.shapes.small,
        sheetContent = {
            FiltersView(
                topPanelFilter, maxReadyTimeFilter, dietsChips, intolerancesChips,
                ingredientsListFilter, minMaxCaloriesInputFilter
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
                is RecipesListState.Success -> {
                    pagingState.value = recipesValue.pagingState
                    PagingList(
                        itemsList = recipesValue.data,
                        itemComponent = {
                            RecipeListItem(
                                recipe = it,
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
                        },
                        pageSize = Constants.RECIPES_LIST_PAGE_SIZE,
                        pagingState = pagingState,
                        getData = {
                            vm.onUIEvent(
                                RecipesListUIEvent.ListScrolledToLoadMoreDataPosition(
                                    it
                                )
                            )
                        }
                    )
                }
                is RecipesListState.Error -> Text(recipesValue.exception.toString())
                else -> Text("")
            }
        }
    }
}

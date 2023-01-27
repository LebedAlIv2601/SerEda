package com.disgust.sereda.ingredients.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.disgust.sereda.ingredients.screens.search.components.IngredientItemComponent
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListState
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListUIEvent
import com.disgust.sereda.utils.Constants
import com.disgust.sereda.utils.components.PagingList
import com.disgust.sereda.utils.components.SearchView

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchIngredientScreen(
    vm: SearchIngredientViewModel
) {
    val ingredientsState = vm.ingredientListState.collectAsState()
    val inputText = vm.inputText.collectAsState()
    val showKeyboard = vm.showKeyboard.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SearchView(
            value = inputText.value,
            onSearch = {
                vm.onUIEvent(IngredientsListUIEvent.SearchClick(inputText.value))
            },
            onValueChange = {
                vm.onUIEvent(IngredientsListUIEvent.InputTextChange(it))
            },
            showKeyboardValue = showKeyboard.value,
            setShowKeyboard = {
                vm.onUIEvent(IngredientsListUIEvent.KeyboardInitShow)
            }
        )

        when (val ingredientsStateValue = ingredientsState.value) {
            is IngredientsListState.Loading -> Text("Loading")
            is IngredientsListState.Success -> {
                PagingList(
                    itemsList = ingredientsStateValue.data,
                    itemComponent = {
                        IngredientItemComponent(it) { ingredient ->
                            vm.onUIEvent(
                                IngredientsListUIEvent.ListItemClick(
                                    item = ingredient
                                )
                            )
                        }
                    },
                    pageSize = Constants.INGREDIENTS_LIST_PAGE_SIZE,
                    pagingState = ingredientsStateValue.pagingState,
                    getData = { nextPage ->
                        vm.onUIEvent(
                            IngredientsListUIEvent.ListScrolledToLoadMoreDataPosition(
                                nextPage
                            )
                        )
                    }
                )
            }
            is IngredientsListState.Error -> Text("Error")
            else -> Text("")
        }
    }
}

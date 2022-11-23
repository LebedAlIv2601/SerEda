package com.disgust.sereda.ingredients.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListState
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListUIEvent
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem
import com.disgust.sereda.utils.commonViews.SearchView

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

        when (ingredientsState.value) {
            is IngredientsListState.Loading -> Text("Loading")
            is IngredientsListState.Success ->
                IngredientsList((ingredientsState.value as IngredientsListState.Success).data) {
                    vm.onUIEvent(
                        IngredientsListUIEvent.ListItemClick(
                            item = it
                        )
                    )
                }
            is IngredientsListState.Error -> Text("Error")
            else -> Text("")
        }
    }
}

@Composable
fun IngredientsList(ingredients: List<IngredientItem>, onItemClick: (IngredientItem) -> Unit) {
    LazyColumn {
        items(ingredients) { ingredient ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onItemClick(ingredient) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = ingredient.name)
            }
        }
    }
}
package com.disgust.sereda.ingredients.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListState
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListUIEvent
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem


@ExperimentalComposeUiApi
@Composable
fun SearchIngredientScreen(
    navController: NavHostController,
    vm: SearchIngredientViewModel = hiltViewModel()
) {
    val ingredientsState = vm.ingredientListState.collectAsState()
    val inputText = vm.inputText.collectAsState()
    val showKeyboard = vm.showKeyboard.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TextField(
            value = inputText.value,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                vm.onUIEvent(IngredientsListUIEvent.SearchClick(inputText.value))
            }),
            onValueChange = { vm.onUIEvent(IngredientsListUIEvent.InputTextChange(it)) },
            modifier = Modifier.focusRequester(focusRequester)
        )

        when (ingredientsState.value) {
            is IngredientsListState.Loading -> Text("Loading")
            is IngredientsListState.Success ->
                IngredientsList((ingredientsState.value as IngredientsListState.Success).data) {
                    vm.onUIEvent(
                        IngredientsListUIEvent.ListItemClick(
                            navController = navController,
                            item = it
                        )
                    )
                }
            is IngredientsListState.Error -> Text("Error")
            else -> Text("")
        }

    }

    LaunchedEffect(focusRequester) {
        if (showKeyboard.value) {
            focusRequester.requestFocus()
            keyboard?.show()
            vm.onUIEvent(IngredientsListUIEvent.KeyboardInitShow)
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
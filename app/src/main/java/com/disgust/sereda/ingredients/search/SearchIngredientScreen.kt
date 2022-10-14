package com.disgust.sereda.ingredients.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController


@ExperimentalComposeUiApi
@Composable
fun SearchIngredientScreen(
    navController: NavHostController,
    vm: SearchIngredientViewModel = hiltViewModel()
) {
    val ingredients = vm.ingredientList.collectAsState()

    val showKeyboard = remember { mutableStateOf(true) }
    val inputText = remember { mutableStateOf("") }
    val lastQuery = remember { mutableStateOf("") }
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
                if (inputText.value.isNotBlank()
                    && (lastQuery.value != inputText.value
                            || ingredients.value is IngredientsListState.Error)
                ) {
                    vm.getIngredients(inputText.value)
                    lastQuery.value = inputText.value
                }
            }),
            onValueChange = { inputText.value = it },
            modifier = Modifier.focusRequester(focusRequester)
        )
        Text(
            text =
            when (ingredients.value) {
                is IngredientsListState.Loading -> "Loading"
                is IngredientsListState.Success -> (ingredients.value as IngredientsListState.Success).data.toString()
                is IngredientsListState.Error -> "Error"
                else -> ""
            }
        )
    }

    LaunchedEffect(focusRequester) {
        if (showKeyboard.value) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
}
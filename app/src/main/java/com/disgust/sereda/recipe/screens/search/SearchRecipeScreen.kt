package com.disgust.sereda.recipe.screens.search

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
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.DoOnInit

@ExperimentalComposeUiApi
@Composable
fun SearchRecipeScreen(
    navController: NavHostController,
    vm: SearchRecipeViewModel = hiltViewModel()
) {
    val recipesState = vm.recipesListState.collectAsState()
    val inputText = vm.inputText.collectAsState()
    val showKeyboard = vm.showKeyboard.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    DoOnInit {
        if (recipesState.value == RecipesListState.Waiting)
            vm.onUIEvent(RecipesListUIEvent.StartScreen)
    }

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
                vm.onUIEvent(RecipesListUIEvent.SearchClick(inputText.value))
            }),
            onValueChange = { vm.onUIEvent(RecipesListUIEvent.InputTextChange(it)) },
            modifier = Modifier.focusRequester(focusRequester)
        )

        when (recipesState.value) {
            is RecipesListState.Loading -> Text("Loading")
            is RecipesListState.Success ->
                RecipesList((recipesState.value as RecipesListState.Success).data) {
                    vm.onUIEvent(
                        RecipesListUIEvent.ListItemClick(
                            navController = navController,
                            item = it
                        )
                    )
                }
            is RecipesListState.Error -> Text("Error")
            else -> Text("")
        }

    }

    LaunchedEffect(focusRequester) {
        if (showKeyboard.value) {
            focusRequester.requestFocus()
            keyboard?.show()
            vm.onUIEvent(RecipesListUIEvent.KeyboardInitShow)
        }
    }
}

@Composable
fun RecipesList(recipes: List<RecipeItem>, onItemClick: (RecipeItem) -> Unit) {
    LazyColumn {
        items(recipes) { recipe ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable { onItemClick(recipe) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = recipe.name)
            }
        }
    }
}
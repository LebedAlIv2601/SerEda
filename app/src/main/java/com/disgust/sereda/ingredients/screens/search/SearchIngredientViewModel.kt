package com.disgust.sereda.ingredients.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.ingredients.data.SearchIngredientRepository
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListState
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListUIEvent
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@HiltViewModel
class SearchIngredientViewModel @Inject constructor(
    private val repository: SearchIngredientRepository
) : NavigatorViewModel(), UIEventHandler<IngredientsListUIEvent> {

    private val _ingredientsListState =
        MutableStateFlow<IngredientsListState>(IngredientsListState.Waiting)
    val ingredientListState = _ingredientsListState.asStateFlow()

    private val _showKeyboard =
        MutableStateFlow(true)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _inputText =
        MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val lastQuery = mutableStateOf("")

    private fun getIngredients(query: String) {
        doSingleRequest(
            query = { repository.getIngredients(query) },
            doOnLoading = {
                _ingredientsListState.value = IngredientsListState.Loading
            },
            doOnSuccess = {
                lastQuery.value = query
                _ingredientsListState.value = IngredientsListState.Success(it)
            },
            doOnError = {
                _ingredientsListState.value = IngredientsListState.Error(it)
            }
        )
    }

    override fun onUIEvent(event: IngredientsListUIEvent) {
        when (event) {
            is IngredientsListUIEvent.SearchClick -> {
                if (event.query.isNotBlank()
                    && lastQuery.value != event.query
                ) {
                    getIngredients(query = event.query)
                }
            }

            is IngredientsListUIEvent.ListItemClick -> {
                navigateWithArguments(
                    destination = Screen.IngredientInfo.route,
                    arguments = mapOf(
                        "ingredientId" to event.item.id.toString(),
                        "ingredientName" to event.item.name
                    )
                )
            }

            is IngredientsListUIEvent.InputTextChange -> {
                _inputText.value = event.text
            }

            is IngredientsListUIEvent.KeyboardInitShow -> {
                _showKeyboard.value = false
            }
        }
    }
}

package com.disgust.sereda.ingredients.screens.search

import androidx.compose.runtime.mutableStateOf
import com.disgust.sereda.ingredients.data.SearchIngredientRepository
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListState
import com.disgust.sereda.ingredients.screens.search.interaction.IngredientsListUIEvent
import com.disgust.sereda.utils.base.BaseViewModel
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.navigation.navigateWithArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchIngredientViewModel @Inject constructor(
    private val repository: SearchIngredientRepository
) : BaseViewModel<IngredientsListUIEvent>() {

    private val _ingredientsListState =
        MutableStateFlow<IngredientsListState>(IngredientsListState.Waiting)
    val ingredientListState = _ingredientsListState.asStateFlow()

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

    override fun onEvent(event: IngredientsListUIEvent) {
        when (event) {
            is IngredientsListUIEvent.SearchClick -> {
                if (event.query.isNotBlank()
                    && (lastQuery.value != event.query
                            || ingredientListState.value is IngredientsListState.Error)
                ) {
                    getIngredients(query = event.query)
                }
            }

            is IngredientsListUIEvent.ListItemClick -> {
                event.navController.navigateWithArguments(
                    destination = Screen.IngredientInfo.route,
                    arguments = mapOf("ingredientId" to event.item.id.toString())
                )
            }
        }
    }
}

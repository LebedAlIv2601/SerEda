package com.disgust.sereda.ingredients.search

import androidx.lifecycle.ViewModel
import com.disgust.sereda.ingredients.search.data.SearchIngredientRepository
import com.disgust.sereda.ingredients.search.model.IngredientItem
import com.disgust.sereda.utils.doSingleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchIngredientViewModel @Inject constructor(
    private val repository: SearchIngredientRepository
) : ViewModel() {

    private val _ingredientsList =
        MutableStateFlow<IngredientsListState>(IngredientsListState.Waiting)
    val ingredientList = _ingredientsList.asStateFlow()

    fun getIngredients(query: String) {
        doSingleRequest(
            query = { repository.getIngredients(query) },
            doOnLoading = {
                _ingredientsList.value = IngredientsListState.Loading
            },
            doOnSuccess = {
                _ingredientsList.value = IngredientsListState.Success(it)
            },
            doOnError = {
                _ingredientsList.value = IngredientsListState.Error(it)
            }
        )
    }
}

sealed class IngredientsListState {
    object Waiting : IngredientsListState()
    object Loading : IngredientsListState()
    class Success(val data: List<IngredientItem>) : IngredientsListState()
    class Error(val exception: Exception) : IngredientsListState()
}
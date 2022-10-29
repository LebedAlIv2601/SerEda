package com.disgust.sereda.ingredients.screens.info

import androidx.lifecycle.ViewModel
import com.disgust.sereda.ingredients.data.SearchIngredientRepository
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoState
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoUIEvent
import com.disgust.sereda.utils.base.EventHandler
import com.disgust.sereda.utils.doSingleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IngredientInfoViewModel @Inject constructor(
    private val repository: SearchIngredientRepository
) : ViewModel(), EventHandler<IngredientInfoUIEvent> {

    private val _ingredientInfoState =
        MutableStateFlow<IngredientInfoState>(IngredientInfoState.Loading)
    val ingredientInfoState = _ingredientInfoState.asStateFlow()

    override fun onEvent(event: IngredientInfoUIEvent) {
        when (event) {
            is IngredientInfoUIEvent.StartScreen -> getIngredientInfo(event.id)
            is IngredientInfoUIEvent.ButtonRestartClick -> getIngredientInfo(event.id)
        }
    }

    private fun getIngredientInfo(id: Int) {
        doSingleRequest(
            query = { repository.getIngredientsInfo(id) },
            doOnLoading = { _ingredientInfoState.value = IngredientInfoState.Loading },
            doOnSuccess = { _ingredientInfoState.value = IngredientInfoState.Success(it) },
            doOnError = { _ingredientInfoState.value = IngredientInfoState.Error(it) }
        )
    }
}
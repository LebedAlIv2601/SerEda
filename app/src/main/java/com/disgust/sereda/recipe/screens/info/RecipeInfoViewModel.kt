package com.disgust.sereda.recipe.screens.info

import androidx.lifecycle.ViewModel
import com.disgust.sereda.recipe.data.InfoRecipeRepository
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.utils.base.EventHandler
import com.disgust.sereda.utils.doSingleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val repository: InfoRecipeRepository
) : ViewModel(), EventHandler<RecipeInfoUIEvent> {

    private val _recipeInfoState =
        MutableStateFlow<RecipeInfoState>(RecipeInfoState.Loading)
    val recipeInfoState = _recipeInfoState.asStateFlow()

    override fun onEvent(event: RecipeInfoUIEvent) {
        when (event) {
            is RecipeInfoUIEvent.StartScreen -> getRecipeInfo(event.id)
            is RecipeInfoUIEvent.ButtonRestartClick -> getRecipeInfo(event.id)
        }
    }

    private fun getRecipeInfo(id: Int) {
        doSingleRequest(
            query = { repository.getInfoRecipe(id) },
            doOnLoading = { _recipeInfoState.value = RecipeInfoState.Loading },
            doOnSuccess = { _recipeInfoState.value = RecipeInfoState.Success(it) },
            doOnError = { _recipeInfoState.value = RecipeInfoState.Error(it) }
        )
    }
}
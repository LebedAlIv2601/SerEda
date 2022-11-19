package com.disgust.sereda.recipe.screens.info

import androidx.lifecycle.ViewModel
import com.disgust.sereda.recipe.commonModel.RecipeFavoriteState
import com.disgust.sereda.recipe.data.RecipeRepository
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.doSingleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel(), UIEventHandler<RecipeInfoUIEvent> {

    private val _recipeInfoState =
        MutableStateFlow<RecipeInfoState>(RecipeInfoState.Loading)
    val recipeInfoState = _recipeInfoState.asStateFlow()

    override fun onUIEvent(event: RecipeInfoUIEvent) {
        when (event) {
            is RecipeInfoUIEvent.StartScreen -> getRecipeInfo(event.id, event.state)
            is RecipeInfoUIEvent.ButtonRestartClick -> getRecipeInfo(event.id)
            is RecipeInfoUIEvent.ButtonAddToFavoriteClick -> {
                if (_recipeInfoState.value is RecipeInfoState.Success) {
                    val recipe = (_recipeInfoState.value as RecipeInfoState.Success).data
                    if (recipe.favoriteState == RecipeFavoriteState.FAVORITE) {
                        deleteRecipeFromFavorite(recipe)
                    } else if (recipe.favoriteState == RecipeFavoriteState.NOT_FAVORITE) {
                        addRecipeToFavorite(recipe)
                    }
                }
            }
        }
    }

    private fun addRecipeToFavorite(recipe: RecipeInfo) {
        changeRecipeState(RecipeFavoriteState.FAVORITE)
        doSingleRequest(
            query = { repository.addFavoriteRecipe(recipe) },
            doOnSuccess = {}
        )
    }

    private fun deleteRecipeFromFavorite(recipe: RecipeInfo) {
        changeRecipeState(RecipeFavoriteState.NOT_FAVORITE)
        doSingleRequest(
            query = { repository.deleteFavoriteRecipe(recipe) },
            doOnSuccess = {}
        )
    }

    private fun getRecipeInfo(id: Int, state: Int? = null) {
        doSingleRequest(
            query = { repository.getInfoRecipe(id) },
            doOnLoading = { _recipeInfoState.value = RecipeInfoState.Loading },
            doOnSuccess = {
                _recipeInfoState.value =
                    RecipeInfoState.Success(
                        if (state != null) {
                            it.copy(favoriteState = RecipeFavoriteState.values()[state])
                        } else {
                            it
                        }
                    )
            },
            doOnError = { _recipeInfoState.value = RecipeInfoState.Error(it) }
        )
    }

    private fun changeRecipeState(favoriteState: RecipeFavoriteState) {
        if (_recipeInfoState.value is RecipeInfoState.Success) {
            _recipeInfoState.value = RecipeInfoState.Success(
                (_recipeInfoState.value as RecipeInfoState.Success).data.copy(favoriteState = favoriteState)
            )
        }
    }
}
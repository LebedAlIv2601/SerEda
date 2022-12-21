package com.disgust.sereda.recipe.screens.info

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.recipe.data.RecipeRepository
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoState
import com.disgust.sereda.recipe.screens.info.interaction.RecipeInfoUIEvent
import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.utils.base.BaseViewModel
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.commonModel.UserNotAuthDialogState
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@HiltViewModel
class RecipeInfoViewModel @Inject constructor(
    private val repository: RecipeRepository
) : BaseViewModel<RecipeInfoUIEvent>() {

    private val _recipeInfoState =
        MutableStateFlow<RecipeInfoState>(RecipeInfoState.Loading)
    val recipeInfoState = _recipeInfoState.asStateFlow()

    private val _userNotAuthDialogState = MutableStateFlow(UserNotAuthDialogState.HIDDEN)
    val userNotAuthDialogState = _userNotAuthDialogState.asStateFlow()

    override fun onUIEvent(event: RecipeInfoUIEvent) {
        when (event) {
            is RecipeInfoUIEvent.StartScreen -> getRecipeInfo(event.id, event.state)
            is RecipeInfoUIEvent.ButtonRestartClick -> getRecipeInfo(event.id)
            is RecipeInfoUIEvent.ButtonAddToFavoriteClick -> {
                if (isAuth()) {
                    _recipeInfoState.value.doAsStateIfPossible<RecipeInfoState.Success> { state ->
                        val recipe = state.data
                        if (recipe.favoriteState == RecipeFavoriteState.FAVORITE) {
                            deleteRecipeFromFavorite(recipe)
                        } else if (recipe.favoriteState == RecipeFavoriteState.NOT_FAVORITE) {
                            addRecipeToFavorite(recipe)
                        }
                    }
                } else {
                    _userNotAuthDialogState.value = UserNotAuthDialogState.SHOWN
                }
            }
            is RecipeInfoUIEvent.UserNotAuthDialogDismiss -> {
                _userNotAuthDialogState.value = UserNotAuthDialogState.HIDDEN
            }
            is RecipeInfoUIEvent.UserNotAuthDialogConfirmButtonClick -> {
                _userNotAuthDialogState.value = UserNotAuthDialogState.HIDDEN
                navigate(Screen.GoogleAuth.route)
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
        _recipeInfoState.value.doAsStateIfPossible<RecipeInfoState.Success> {
            _recipeInfoState.value = RecipeInfoState.Success(
                it.data.copy(favoriteState = favoriteState)
            )
        }
    }

    private fun isAuth() = repository.isAuth()
}
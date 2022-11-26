package com.disgust.sereda.favorite.favoriteList

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.favorite.data.FavoriteRepository
import com.disgust.sereda.favorite.favoriteList.interaction.FavoriteRecipesListState
import com.disgust.sereda.favorite.favoriteList.interaction.FavoriteUIEvent
import com.disgust.sereda.favorite.favoriteList.model.FavoriteRecipe
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.subscribeToFlowOnIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) :
    NavigatorViewModel(), UIEventHandler<FavoriteUIEvent> {

    private val _recipesListState =
        MutableStateFlow<FavoriteRecipesListState>(FavoriteRecipesListState.Waiting)
    val recipesListState = _recipesListState.asStateFlow()

    init {
        subscribeToFavoriteRecipes()
    }

    override fun onUIEvent(event: FavoriteUIEvent) {
        when (event) {
            is FavoriteUIEvent.StartScreen -> {
                if (_recipesListState.value !is FavoriteRecipesListState.Updated
                    && _recipesListState.value !is FavoriteRecipesListState.NotUpdated
                ) {
                    updateFavoriteRecipes()
                }
            }
            is FavoriteUIEvent.FavoriteRecipesListItemClick -> {
                navigateWithArguments(
                    Screen.RecipeInfo.route,
                    mapOf(
                        "recipeId" to event.recipe.id.toString(),
                        "favoriteState" to RecipeFavoriteState.FAVORITE.ordinal.toString()
                    )
                )
            }
            is FavoriteUIEvent.DeleteFromFavoriteButtonClick -> {
                deleteRecipeFromFavorite(event.recipe)
            }
            is FavoriteUIEvent.UpdateButtonClick -> {
                updateFavoriteRecipes()
            }
        }
    }

    private fun deleteRecipeFromFavorite(recipe: FavoriteRecipe) {
        doSingleRequest(
            query = { repository.deleteRecipeFromFavorite(recipe) },
            doOnSuccess = {}
        )
    }

    private fun updateFavoriteRecipes() {
        doSingleRequest(
            query = { repository.updateLocalData() },
            doOnSuccess = { recipes ->
                _recipesListState.value.doAsStateIfPossible<FavoriteRecipesListState.Updating> {
                    if (recipes.isEmpty() && it.data.isEmpty()) {
                        _recipesListState.value = FavoriteRecipesListState.Updated(recipes)
                    }
                }
            },
            doOnLoading = {
                _recipesListState.value.doAsStateIfPossible<FavoriteRecipesListState.Updated> {
                    _recipesListState.value = FavoriteRecipesListState.Updating(it.data)
                }
            },
            doOnError = {
                when (val recipesState = _recipesListState.value) {
                    is FavoriteRecipesListState.Updating -> {
                        _recipesListState.value =
                            FavoriteRecipesListState.NotUpdated(recipesState.data)
                    }
                    is FavoriteRecipesListState.Updated -> {
                        _recipesListState.value =
                            FavoriteRecipesListState.NotUpdated(recipesState.data)
                    }
                    else -> {}
                }
            }
        )
    }

    private fun subscribeToFavoriteRecipes() {
        subscribeToFlowOnIO(
            flowToCollect = { repository.getFavoriteRecipes() },
            doOnCollect = { _recipesListState.value = FavoriteRecipesListState.Updated(it) },
            doOnLoading = { _recipesListState.value = FavoriteRecipesListState.Loading },
            doOnError = { _recipesListState.value = FavoriteRecipesListState.Error(it) }
        )
    }
}

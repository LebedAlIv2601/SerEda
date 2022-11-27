package com.disgust.sereda.recipe.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.recipe.data.RecipeRepository
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.recipe.screens.search.model.Diet
import com.disgust.sereda.recipe.screens.search.model.FiltersRecipe
import com.disgust.sereda.recipe.screens.search.model.IngredientFilter
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
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

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : NavigatorViewModel(), UIEventHandler<RecipesListUIEvent> {

    private val _recipesListState =
        MutableStateFlow<RecipesListState>(RecipesListState.Waiting)
    val recipesListState = _recipesListState.asStateFlow()

    private val _showKeyboard =
        MutableStateFlow(false)
    val showKeyboard = _showKeyboard.asStateFlow()

    private val _hideKeyboard =
        MutableStateFlow(false)
    val hideKeyboard = _hideKeyboard.asStateFlow()

    private val _inputText =
        MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val lastQuery = mutableStateOf("")

    private val filtersRecipe =
        MutableStateFlow(FiltersRecipe(null, null))
    private val builderFilters = FiltersRecipe.Builder()

    private val _ingredientsList =
        MutableStateFlow(listOf<IngredientFilter>())
    val ingredientsList = _ingredientsList.asStateFlow()

    private val _dietsList =
        MutableStateFlow(listOf<Diet>())
    val dietList = _dietsList.asStateFlow()

    init {
        subscribeToFavoriteRecipesIds()
        updateFavoriteIds()
    }

    override fun onUIEvent(event: RecipesListUIEvent) {
        when (event) {
            is RecipesListUIEvent.SearchClick -> {
                if (event.query.isNotBlank()
                    && lastQuery.value != event.query
                ) {
                    getRecipes(query = event.query)
                }
            }

            is RecipesListUIEvent.ListItemClick -> {
                navigateWithArguments(
                    destination = Screen.RecipeInfo.route,
                    arguments = mapOf(
                        "recipeId" to event.item.id.toString(),
                        "favoriteState" to event.item.favoriteState.ordinal.toString()
                    )
                )
            }

            is RecipesListUIEvent.InputTextChange -> {
                _inputText.value = event.text
            }

            is RecipesListUIEvent.KeyboardInitShow -> {
                _showKeyboard.value = false
            }

            is RecipesListUIEvent.StartScreen -> {
                getFiltersIngredients()
                if (_recipesListState.value is RecipesListState.Waiting) {
                    getRandomRecipes()
                }
            }

            is RecipesListUIEvent.ListItemButtonAddToFavoriteClick -> {
                if (event.recipe.favoriteState == RecipeFavoriteState.NOT_FAVORITE) {
                    addRecipeToFavorite(event.recipe)
                } else if (event.recipe.favoriteState == RecipeFavoriteState.FAVORITE) {
                    deleteRecipeFromFavorite(event.recipe)
                }
            }

            is RecipesListUIEvent.FiltersApplyButtonClick -> {
                val oldFilters = filtersRecipe.value
                filtersRecipe.value = builderFilters.build()
                if (oldFilters != filtersRecipe.value)
                    getRecipes(query = event.query)
            }

            is RecipesListUIEvent.FiltersSearchIngredientButtonClick -> {
                navigate(Screen.SearchIngredient.route)
            }

            is RecipesListUIEvent.FiltersDeleteAllIngredients -> {
                builderFilters.setIngredientsList(mutableListOf())
                _ingredientsList.value = mutableListOf()
            }

            is RecipesListUIEvent.FiltersDeleteIngredient -> {
                builderFilters.deleteIngredient(event.item)
                _ingredientsList.value = builderFilters.ingredientsList
            }

            is RecipesListUIEvent.FiltersOpenButtonClick -> {
                _hideKeyboard.value = true
                _ingredientsList.value = filtersRecipe.value.ingredientsList ?: listOf()
                _dietsList.value = filtersRecipe.value.dietsList ?: listOf()
                builderFilters
                    .setIngredientsList(
                        filtersRecipe.value.ingredientsList?.toMutableList() ?: mutableListOf()
                    )
                    .setDietsList(filtersRecipe.value.dietsList?.toMutableList() ?: mutableListOf())
            }

            is RecipesListUIEvent.KeyboardSetHide -> {
                _hideKeyboard.value = false
            }

            is RecipesListUIEvent.ProfileButtonClick -> {
                navigate(Screen.Profile.route)
            }

            is RecipesListUIEvent.FiltersSetDiet -> {
                if (event.isAdd) {
                    builderFilters.addDiet(event.diet)
                    _dietsList.value = builderFilters.dietsList.map { it }
                } else {
                    builderFilters.deleteDiet(event.diet)
                    _dietsList.value = builderFilters.dietsList.map { it }
                }
            }
            is RecipesListUIEvent.FavoriteListButtonClick -> {
                navigate(Screen.Favorite.route)
            }
        }
    }


    private fun getFiltersIngredients() {
        doSingleRequest(
            query = { repository.getFiltersIngredientsRecipe() },
            doOnSuccess = {
                builderFilters.addIngredient(it.first())
                _ingredientsList.value = _ingredientsList.value + it
            }
        )
    }

    private fun subscribeToFavoriteRecipesIds() {
        subscribeToFlowOnIO(
            flowToCollect = { repository.getFavoriteRecipeIdsFlow() },
            doOnCollect = { updateListWithFavoriteIds(it) }
        )
    }

    private fun updateListWithFavoriteIds(favoriteIds: List<Int>) {
        _recipesListState.value.doAsStateIfPossible<RecipesListState.Success> { state ->
            val recipesList = state.data.toMutableList()
            recipesList.forEachIndexed { index, recipe ->
                val isFavorite = favoriteIds.find { it == recipe.id } != null
                if (isFavorite) {
                    recipesList[index] =
                        recipe.copy(favoriteState = RecipeFavoriteState.FAVORITE)
                } else {
                    recipesList[index] =
                        recipe.copy(favoriteState = RecipeFavoriteState.NOT_FAVORITE)
                }
            }
            _recipesListState.value = RecipesListState.Success(recipesList)
        }
    }

    private fun updateFavoriteIds() {
        doSingleRequest(
            query = { repository.updateFavoriteRecipeIds() },
            doOnSuccess = {}
        )
    }

    private fun getRecipes(query: String) {
        doSingleRequest(
            query = {
                repository.searchRecipes(
                    query = query,
                    includeIngredients = filtersRecipe.value.ingredientsList?.filter { it.isInclude }
                        .toString(),
                    excludeIngredients = filtersRecipe.value.ingredientsList?.filter { !it.isInclude }
                        .toString(),
                    diet = filtersRecipe.value.dietsList.toString())
            },
            doOnLoading = {
                _recipesListState.value = RecipesListState.Loading
            },
            doOnSuccess = {
                lastQuery.value = query
                _recipesListState.value = RecipesListState.Success(it)
            },
            doOnError = {
                _recipesListState.value = RecipesListState.Error(it)
            }
        )
    }

    private fun getRandomRecipes() {
        doSingleRequest(
            query = {
                repository.searchRecipes(sort = "random")
            },
            doOnLoading = {
                _recipesListState.value = RecipesListState.Loading
            },
            doOnSuccess = {
                _recipesListState.value = RecipesListState.Success(it)
            },
            doOnError = {
                _recipesListState.value = RecipesListState.Error(it)
            }
        )
    }

    private fun addRecipeToFavorite(recipe: RecipeItem) {
        doSingleRequest(
            query = { repository.addFavoriteRecipe(recipe) },
            doOnSuccess = {}
        )
    }


    private fun deleteRecipeFromFavorite(recipe: RecipeItem) {
        doSingleRequest(
            query = { repository.deleteFavoriteRecipe(recipe) },
            doOnSuccess = {}
        )
    }
}
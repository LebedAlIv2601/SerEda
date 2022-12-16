package com.disgust.sereda.recipe.screens.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.recipe.data.RecipeRepository
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListState
import com.disgust.sereda.recipe.screens.search.interaction.RecipesListUIEvent
import com.disgust.sereda.recipe.screens.search.model.*
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.commonModel.UserNotAuthDialogState
import com.disgust.sereda.utils.components.PagingState
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import com.disgust.sereda.utils.subscribeToFlowOnIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        MutableStateFlow(FiltersRecipe())
    private val builderFilters = FiltersRecipe.Builder()

    private val _ingredientsList =
        MutableStateFlow(listOf<IngredientFilter>())
    val ingredientsList = _ingredientsList.asStateFlow()

    private val _dietsList =
        MutableStateFlow(listOf<Diet>())
    val dietList = _dietsList.asStateFlow()

    private val _intolerancesList =
        MutableStateFlow(listOf<Intolerance>())
    val intolerancesList = _intolerancesList.asStateFlow()

    private val _maxReadyTime =
        MutableStateFlow<Int?>(null)
    val maxReadyTime = _maxReadyTime.asStateFlow()

    private val _minCalories =
        MutableStateFlow<Int?>(null)
    val minCalories = _minCalories.asStateFlow()

    private val _maxCalories =
        MutableStateFlow<Int?>(null)
    val maxCalories = _maxCalories.asStateFlow()

    private val _userNotAuthDialogState = MutableStateFlow(UserNotAuthDialogState.HIDDEN)
    val userNotAuthDialogState = _userNotAuthDialogState.asStateFlow()

    init {
        subscribeToFavoriteRecipesIds()
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
                updateFavoriteIds()
                getFiltersIngredients()
                if (_recipesListState.value is RecipesListState.Waiting) {
                    getRandomRecipes()
                }
            }

            is RecipesListUIEvent.ListItemButtonAddToFavoriteClick -> {
                if (isAuth()) {
                    if (event.recipe.favoriteState == RecipeFavoriteState.NOT_FAVORITE) {
                        addRecipeToFavorite(event.recipe)
                    } else if (event.recipe.favoriteState == RecipeFavoriteState.FAVORITE) {
                        deleteRecipeFromFavorite(event.recipe)
                    }
                } else {
                    _userNotAuthDialogState.value = UserNotAuthDialogState.SHOWN
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
                _intolerancesList.value = filtersRecipe.value.intolerancesList ?: listOf()
                _maxReadyTime.value = filtersRecipe.value.maxReadyTime
                _minCalories.value = filtersRecipe.value.minCalories
                _maxCalories.value = filtersRecipe.value.maxCalories
                builderFilters
                    .setIngredientsList(
                        filtersRecipe.value.ingredientsList?.toMutableList() ?: mutableListOf()
                    )
                    .setDietsList(
                        filtersRecipe.value.dietsList?.toMutableList() ?: mutableListOf()
                    )
                    .setIntolerancesList(
                        filtersRecipe.value.intolerancesList?.toMutableList() ?: mutableListOf()
                    )
                    .setMaxReadyTime(filtersRecipe.value.maxReadyTime)
                    .setMinCalories(filtersRecipe.value.minCalories)
                    .setMaxCalories(filtersRecipe.value.maxCalories)
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

            is RecipesListUIEvent.FiltersSetIntolerance -> {
                if (event.isAdd) {
                    builderFilters.addIntolerance(event.intolerance)
                    _intolerancesList.value = builderFilters.intolerancesList.map { it }
                } else {
                    builderFilters.deleteIntolerance(event.intolerance)
                    _intolerancesList.value = builderFilters.intolerancesList.map { it }
                }
            }

            is RecipesListUIEvent.FiltersInputReadyTimeChange -> {
                _maxReadyTime.value = event.value.toIntOrNull()
                builderFilters.setMaxReadyTime(_maxReadyTime.value)
            }

            is RecipesListUIEvent.FiltersInputMinCaloriesChange -> {
                _minCalories.value = event.value.toIntOrNull()
                builderFilters.setMinCalories(_minCalories.value)
            }

            is RecipesListUIEvent.FiltersInputMaxCaloriesChange -> {
                _maxCalories.value = event.value.toIntOrNull()
                builderFilters.setMaxCalories(_maxCalories.value)
            }

            is RecipesListUIEvent.FavoriteListButtonClick -> {
                navigate(Screen.Favorite.route)
            }

            is RecipesListUIEvent.UserNotAuthDialogDismiss -> {
                _userNotAuthDialogState.value = UserNotAuthDialogState.HIDDEN
            }

            is RecipesListUIEvent.UserNotAuthDialogConfirmButtonClick -> {
                _userNotAuthDialogState.value = UserNotAuthDialogState.HIDDEN
                navigate(Screen.GoogleAuth.route)
            }

            is RecipesListUIEvent.ListScrolledToLoadMoreDataPosition -> {
                getMoreRecipes(event.loadedItems)
            }
        }
    }

    private fun getMoreRecipes(loadedItems: Int) {
        doSingleRequest(
            query = {
                repository.searchRecipes(
                    query = lastQuery.value,
                    includeIngredients = filtersRecipe.value.ingredientsList?.filter { it.isInclude }
                        .toString(),
                    excludeIngredients = filtersRecipe.value.ingredientsList?.filter { !it.isInclude }
                        .toString(),
                    diet = filtersRecipe.value.dietsList?.map { it.value }.toString(),
                    offset = loadedItems
                )
            },
            doOnSuccess = {
                _recipesListState.update { prevState ->
                    val list = (prevState as RecipesListState.Success).data.toMutableList()
                    if (it.isNotEmpty()) {
                        list.addAll(it)
                        RecipesListState.Success(list, PagingState.Success(false))
                    } else {
                        RecipesListState.Success(list, PagingState.Success(true))
                    }
                }
            },
            doOnError = {
                _recipesListState.update { prevState ->
                    val data = (prevState as RecipesListState.Success).data
                    RecipesListState.Success(data, PagingState.Error)
                }
            },
            doOnLoading = {
                _recipesListState.update { prevState ->
                    val data = (prevState as RecipesListState.Success).data
                    RecipesListState.Success(data, PagingState.Loading)
                }
            }
        )
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
            doOnCollect = {
                if (isAuth()) {
                    updateListWithFavoriteIds(it)
                }
            }
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
                    diet = filtersRecipe.value.dietsList?.map { it.value }.toString(),
                    maxReadyTime = filtersRecipe.value.maxReadyTime,
                    minCalories = filtersRecipe.value.minCalories,
                    maxCalories = filtersRecipe.value.maxCalories
                )
            },
            doOnLoading = {
                _recipesListState.value = RecipesListState.Loading
            },
            doOnSuccess = {
                lastQuery.value = query
                _recipesListState.value = RecipesListState.Success(it, PagingState.Success(false))
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
                _recipesListState.value = RecipesListState.Success(it, PagingState.Success(false))
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

    private fun isAuth(): Boolean {
        return repository.isAuth()
    }
}
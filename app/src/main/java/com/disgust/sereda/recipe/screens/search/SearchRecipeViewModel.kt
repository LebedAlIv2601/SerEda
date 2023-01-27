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
import com.disgust.sereda.utils.toQueryString
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

    private var filtersRecipeApplied = FiltersRecipe()
    private val builderFilters = FiltersRecipe.Builder()
    private val _filtersRecipeChanged =
        MutableStateFlow(FiltersRecipe())
    val filtersRecipeChanged = _filtersRecipeChanged.asStateFlow()

    private val _userNotAuthDialogState = MutableStateFlow(UserNotAuthDialogState.HIDDEN)
    val userNotAuthDialogState = _userNotAuthDialogState.asStateFlow()

    init {
        subscribeToFavoriteRecipesIds()
        filtersDeleteAllIngredients()
    }

    override fun onUIEvent(event: RecipesListUIEvent) {
        when (event) {
            is RecipesListUIEvent.SearchClick -> searchRecipes(event.query)

            is RecipesListUIEvent.ListItemClick -> navigateWithArguments(
                destination = Screen.RecipeInfo.route,
                arguments = mapOf(
                    "recipeId" to event.item.id.toString(),
                    "favoriteState" to event.item.favoriteState.ordinal.toString()
                )
            )

            is RecipesListUIEvent.InputTextChange -> _inputText.value = event.text

            is RecipesListUIEvent.KeyboardInitShow -> _showKeyboard.value = false

            is RecipesListUIEvent.StartScreen -> onStartScreen()

            is RecipesListUIEvent.ListItemButtonAddToFavoriteClick -> changeRecipeFavoriteState(
                event.recipe
            )

            is RecipesListUIEvent.FiltersApplyButtonClick -> filtersApplyButtonClick(event.query)

            is RecipesListUIEvent.FiltersSearchIngredientButtonClick -> navigate(Screen.SearchIngredient.route)

            is RecipesListUIEvent.FiltersDeleteAll -> filtersDeleteAll()

            is RecipesListUIEvent.FiltersDeleteIngredient -> filtersDeleteIngredient(event.item)

            is RecipesListUIEvent.FiltersOpenButtonClick -> onFiltersOpen()

            is RecipesListUIEvent.KeyboardSetHide -> _hideKeyboard.value = false

            is RecipesListUIEvent.ProfileButtonClick -> navigate(Screen.Profile.route)

            is RecipesListUIEvent.FiltersSetDiet -> setDiet(event.diet, event.isAdd)

            is RecipesListUIEvent.FiltersSetIntolerance -> setIntolerance(
                event.intolerance,
                event.isAdd
            )

            is RecipesListUIEvent.FiltersInputReadyTimeChange -> buildFiltersChanged {
                builderFilters.setMaxReadyTime(
                    event.value.toIntOrNull()
                )
            }

            is RecipesListUIEvent.FiltersInputMinCaloriesChange -> buildFiltersChanged {
                builderFilters.setMinCalories(
                    event.value.toIntOrNull()
                )
            }

            is RecipesListUIEvent.FiltersInputMaxCaloriesChange -> buildFiltersChanged {
                builderFilters.setMaxCalories(
                    event.value.toIntOrNull()
                )
            }

            is RecipesListUIEvent.FavoriteListButtonClick -> navigate(Screen.Favorite.route)

            is RecipesListUIEvent.UserNotAuthDialogDismiss -> _userNotAuthDialogState.value =
                UserNotAuthDialogState.HIDDEN

            is RecipesListUIEvent.UserNotAuthDialogConfirmButtonClick -> navigateToAuthScreenFromNotAuthDialog()

            is RecipesListUIEvent.ListScrolledToLoadMoreDataPosition -> getMoreRecipes(event.loadedItems)
        }
    }

    private fun searchRecipes(query: String) {
        if (query.isNotBlank()
            && lastQuery.value != query
        ) {
            getRecipes(query = query)
        }
    }

    private fun changeRecipeFavoriteState(recipe: RecipeItem) {
        if (isAuth()) {
            if (recipe.favoriteState == RecipeFavoriteState.NOT_FAVORITE) {
                addRecipeToFavorite(recipe)
            } else if (recipe.favoriteState == RecipeFavoriteState.FAVORITE) {
                deleteRecipeFromFavorite(recipe)
            }
        } else {
            _userNotAuthDialogState.value = UserNotAuthDialogState.SHOWN
        }
    }

    private fun onStartScreen() {
        updateFavoriteIds()
        getFiltersIngredients()
        if (_recipesListState.value is RecipesListState.Waiting) {
            getRandomRecipes()
        }
    }

    private fun onFiltersOpen() {
        _hideKeyboard.value = true
        restoreFiltersChanged()
    }

    private fun restoreFiltersChanged() {
        buildFiltersChanged {
            builderFilters
                .setIngredientsList(
                    filtersRecipeApplied.ingredientsList
                )
                .setDietsList(
                    filtersRecipeApplied.dietsList
                )
                .setIntolerancesList(
                    filtersRecipeApplied.intolerancesList
                )
                .setMaxReadyTime(filtersRecipeApplied.maxReadyTime)
                .setMinCalories(filtersRecipeApplied.minCalories)
                .setMaxCalories(filtersRecipeApplied.maxCalories)
        }
        updateFiltersIngredients(builderFilters.ingredientsList)
    }

    private fun setDiet(diet: Diet, isAdd: Boolean) {
        buildFiltersChanged {
            if (isAdd) {
                builderFilters.addDiet(diet)
            } else {
                builderFilters.deleteDiet(diet)
            }
        }
    }

    private fun setIntolerance(intolerance: Intolerance, isAdd: Boolean) {
        buildFiltersChanged {
            if (isAdd) {
                builderFilters.addIntolerance(intolerance)
            } else {
                builderFilters.deleteIntolerance(intolerance)
            }
        }
    }

    private fun navigateToAuthScreenFromNotAuthDialog() {
        _userNotAuthDialogState.value = UserNotAuthDialogState.HIDDEN
        navigate(Screen.GoogleAuth.route)
    }

    private fun buildFiltersChanged(changeBuilder: () -> Unit = {}) {
        changeBuilder()
        _filtersRecipeChanged.value = builderFilters.build()
    }

    private fun getMoreRecipes(loadedItems: Int) {
        doSingleRequest(
            query = {
                repository.searchRecipes(
                    query = lastQuery.value,
                    includeIngredients = filtersRecipeApplied.ingredientsList?.filter { it.isInclude }
                        .toString(),
                    excludeIngredients = filtersRecipeApplied.ingredientsList?.filter { !it.isInclude }
                        .toString(),
                    diet = filtersRecipeApplied.dietsList?.map { it.value }.toQueryString(),
                    intolerances = filtersRecipeApplied.intolerancesList?.map { it.value }
                        .toQueryString(),
                    maxReadyTime = filtersRecipeApplied.maxReadyTime,
                    minCalories = filtersRecipeApplied.minCalories,
                    maxCalories = filtersRecipeApplied.maxCalories,
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
            query = { repository.getFiltersIngredientRecipe() },
            doOnSuccess = {
                buildFiltersChanged {
                    builderFilters.addIngredient(it.minus(builderFilters.ingredientsList).first())
                }
            }
        )
    }

    private fun filtersDeleteAll() {
        buildFiltersChanged { builderFilters.clearAll() }
        filtersDeleteAllIngredients()
    }

    private fun filtersDeleteAllIngredients() {
        doSingleRequest(
            query = { repository.deleteAllFiltersRecipe() },
            doOnSuccess = {}
        )
    }

    private fun filtersDeleteIngredient(ingredientFilter: IngredientFilter) {
        doSingleRequest(
            query = { repository.deleteFiltersIngredient(ingredientFilter) },
            doOnSuccess = {
                buildFiltersChanged { builderFilters.deleteIngredient(ingredientFilter) }
            }
        )
    }

    private fun updateFiltersIngredients(list: List<IngredientFilter>) {
        doSingleRequest(
            query = { repository.updateFiltersIngredients(list) },
            doOnSuccess = {}
        )
    }

    private fun filtersApplyButtonClick(query: String) {
        val oldFilters = filtersRecipeApplied
        filtersRecipeApplied = builderFilters.build()
        if (oldFilters != filtersRecipeApplied) {
            getRecipes(query = query)
        }
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
            _recipesListState.value = RecipesListState.Success(recipesList, state.pagingState)
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
                    includeIngredients = filtersRecipeApplied.ingredientsList?.filter { it.isInclude }
                        .toString(),
                    excludeIngredients = filtersRecipeApplied.ingredientsList?.filter { !it.isInclude }
                        .toString(),
                    diet = filtersRecipeApplied.dietsList?.map { it.value }.toQueryString(),
                    intolerances = filtersRecipeApplied.intolerancesList?.map { it.value }
                        .toQueryString(),
                    maxReadyTime = filtersRecipeApplied.maxReadyTime,
                    minCalories = filtersRecipeApplied.minCalories,
                    maxCalories = filtersRecipeApplied.maxCalories
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
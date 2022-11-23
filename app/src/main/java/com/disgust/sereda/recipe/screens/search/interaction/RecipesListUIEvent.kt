package com.disgust.sereda.recipe.screens.search.interaction

import androidx.navigation.NavHostController
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.base.BaseUIEvent
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel

sealed class RecipesListUIEvent : BaseUIEvent {
    class SearchClick(val query: String) : RecipesListUIEvent()
    class ListItemClick(val item: RecipeItem, val navController: NavHostController) :
        RecipesListUIEvent()

    class InputTextChange(val text: String) : RecipesListUIEvent()
    object KeyboardInitShow : RecipesListUIEvent()
    object KeyboardSetHide : RecipesListUIEvent()
    object StartScreen : RecipesListUIEvent()
    class ListItemButtonAddToFavoriteClick(val recipe: RecipeItem) : RecipesListUIEvent()
    class ProfileButtonClick(val navController: NavHostController) :
        RecipesListUIEvent()

    class FiltersApplyButtonClick(val query: String) : RecipesListUIEvent()
    class FiltersSearchIngredientButtonClick(val navController: NavHostController) :
        RecipesListUIEvent()

    object FiltersOpenButtonClick : RecipesListUIEvent()
    object FiltersDeleteAll : RecipesListUIEvent()
    class FiltersDeleteItem(val item: FilterRecipeDBModel) : RecipesListUIEvent()
}
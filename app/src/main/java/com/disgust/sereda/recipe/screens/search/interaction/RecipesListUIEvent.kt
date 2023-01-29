package com.disgust.sereda.recipe.screens.search.interaction

import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.utils.base.BaseUIEvent
import com.disgust.sereda.utils.commonModel.Diet
import com.disgust.sereda.utils.commonModel.IngredientFilter
import com.disgust.sereda.utils.commonModel.Intolerance

sealed class RecipesListUIEvent : BaseUIEvent {
    class SearchClick(val query: String) : RecipesListUIEvent()
    class ListItemClick(val item: RecipeItem) :
        RecipesListUIEvent()

    class InputTextChange(val text: String) : RecipesListUIEvent()
    object KeyboardInitShow : RecipesListUIEvent()
    object KeyboardSetHide : RecipesListUIEvent()
    object StartScreen : RecipesListUIEvent()
    class ListItemButtonAddToFavoriteClick(val recipe: RecipeItem) : RecipesListUIEvent()
    object ProfileButtonClick : RecipesListUIEvent()
    object FavoriteListButtonClick : RecipesListUIEvent()

    class ListScrolledToLoadMoreDataPosition(val loadedItems: Int) : RecipesListUIEvent()

    object UserNotAuthDialogDismiss : RecipesListUIEvent()
    object UserNotAuthDialogConfirmButtonClick : RecipesListUIEvent()

    class FiltersApplyButtonClick(val query: String) : RecipesListUIEvent()
    object FiltersSearchIngredientButtonClick : RecipesListUIEvent()
    object FiltersOpenButtonClick : RecipesListUIEvent()
    object FiltersDeleteAll : RecipesListUIEvent()
    class FiltersDeleteIngredient(val item: IngredientFilter) : RecipesListUIEvent()
    class FiltersSetDiet(val diet: Diet, val isAdd: Boolean) : RecipesListUIEvent()
    class FiltersSetIntolerance(val intolerance: Intolerance, val isAdd: Boolean) :
        RecipesListUIEvent()

    class FiltersInputReadyTimeChange(val value: String) : RecipesListUIEvent()
    class FiltersInputMinCaloriesChange(val value: String) : RecipesListUIEvent()
    class FiltersInputMaxCaloriesChange(val value: String) : RecipesListUIEvent()
}
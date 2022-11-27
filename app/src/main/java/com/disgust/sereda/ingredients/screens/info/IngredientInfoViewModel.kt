package com.disgust.sereda.ingredients.screens.info

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.ingredients.data.SearchIngredientRepository
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoState
import com.disgust.sereda.ingredients.screens.info.interactioin.IngredientInfoUIEvent
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@HiltViewModel
class IngredientInfoViewModel @Inject constructor(
    private val repository: SearchIngredientRepository
) : NavigatorViewModel(), UIEventHandler<IngredientInfoUIEvent> {

    private val _ingredientInfoState =
        MutableStateFlow<IngredientInfoState>(IngredientInfoState.Loading)
    val ingredientInfoState = _ingredientInfoState.asStateFlow()

    private val _enabledButtonFilters =
        MutableStateFlow(false)
    val enabledButtonFilters = _enabledButtonFilters.asStateFlow()

    override fun onUIEvent(event: IngredientInfoUIEvent) {
        when (event) {
            is IngredientInfoUIEvent.StartScreen -> getIngredientInfo(event.id, event.name)
            is IngredientInfoUIEvent.ButtonRestartClick -> {}

            is IngredientInfoUIEvent.IngredientAddButtonClick -> {
                _ingredientInfoState.value.doAsStateIfPossible<IngredientInfoState.Success> {
                    val data = it.data
                    addFilterRecipe(
                        FilterRecipeDBModel(
                            id = data.id,
                            name = data.name,
                            image = data.image,
                            isInclude = true
                        )
                    )
                    popBackStack(Screen.SearchRecipe.route, false)
                }
            }

            is IngredientInfoUIEvent.IngredientExcludeButtonClick -> {
                _ingredientInfoState.value.doAsStateIfPossible<IngredientInfoState.Success> {
                    val data = it.data
                    addFilterRecipe(
                        FilterRecipeDBModel(
                            id = data.id,
                            name = data.name,
                            image = data.image,
                            isInclude = false
                        )
                    )
                    popBackStack(Screen.SearchRecipe.route, false)
                }
            }
        }
    }

    private fun addFilterRecipe(filter: FilterRecipeDBModel) {
        doSingleRequest(
            query = { repository.addFilterRecipe(filter) },
            doOnSuccess = {}
        )
    }

    private fun getIngredientInfo(id: Int, name: String) {
        doSingleRequest(
            query = { repository.getIngredientsInfo(id) },
            doOnLoading = { _ingredientInfoState.value = IngredientInfoState.Loading },
            doOnSuccess = {
                _ingredientInfoState.value = IngredientInfoState.Success(
                    it.copy(name = name)
                )
                _enabledButtonFilters.value = true
            },
            doOnError = { _ingredientInfoState.value = IngredientInfoState.Error(it) }
        )
    }
}
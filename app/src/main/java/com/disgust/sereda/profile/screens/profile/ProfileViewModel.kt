package com.disgust.sereda.profile.screens.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.profile.data.ProfileRepository
import com.disgust.sereda.profile.screens.profile.interaction.ProfileUIEvent
import com.disgust.sereda.profile.screens.profile.interaction.UserInfoState
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.base.UIEventHandler
import com.disgust.sereda.utils.commonModel.Diet
import com.disgust.sereda.utils.commonModel.FiltersRecipe
import com.disgust.sereda.utils.commonModel.Intolerance
import com.disgust.sereda.utils.doSingleRequest
import com.disgust.sereda.utils.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class ProfileViewModel @Inject constructor(val repository: ProfileRepository) :
    NavigatorViewModel(),
    UIEventHandler<ProfileUIEvent> {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Waiting)
    val userInfoState = _userInfoState.asStateFlow()

    private val builderFilters = FiltersRecipe.Builder()
    private val _filtersProfile =
        MutableStateFlow(FiltersRecipe())
    val filtersProfile = _filtersProfile.asStateFlow()

    override fun onUIEvent(event: ProfileUIEvent) {
        when (event) {
            is ProfileUIEvent.StartScreen -> screenStarted()
            is ProfileUIEvent.ButtonSignOutClick -> signOut()
            is ProfileUIEvent.ButtonAuthClick -> navigate(Screen.GoogleAuth.route)
            is ProfileUIEvent.FiltersSetDiet -> setDiet(event.diet, event.isAdd)
            is ProfileUIEvent.FiltersSetIntolerance -> setIntolerance(
                event.intolerance,
                event.isAdd
            )
        }
    }

    private fun screenStarted() {
        if (isAuth()) {
            getProfileData()
            getDiets()
            getIntolerance()
        } else {
            _userInfoState.value = UserInfoState.NotAuth
        }
    }

    private fun isAuth() = repository.isAuth()

    private fun signOut() {
        doSingleRequest(
            query = { repository.signOut() },
            doOnSuccess = { navigateWithClearBackStack(Screen.SearchRecipe.route) },
        )
    }

    private fun getProfileData() {
        doSingleRequest(
            query = { repository.getProfileInfo() },
            doOnSuccess = { _userInfoState.value = UserInfoState.Success(it) },
            doOnLoading = { _userInfoState.value = UserInfoState.Loading }
        )
    }

    private fun setIntolerance(intolerance: Intolerance, isAdd: Boolean) {
        if (isAdd) {
            addIntolerance(intolerance)
        } else {
            deleteIntolerance(intolerance)
        }
    }

    private fun deleteIntolerance(intolerance: Intolerance) {
        doSingleRequest(
            query = { repository.deleteIntolerance(intolerance.value) },
            doOnSuccess = {
                builderFilters.deleteIntolerance(intolerance)
                _filtersProfile.value = builderFilters.build()
            }
        )
    }

    private fun addIntolerance(intolerance: Intolerance) {
        doSingleRequest(
            query = { repository.addIntolerance(intolerance.value) },
            doOnSuccess = {
                builderFilters.addIntolerance(intolerance)
                _filtersProfile.value = builderFilters.build()
            }
        )
    }

    private fun setDiet(diet: Diet, isAdd: Boolean) {
        if (isAdd)
            addDiet(diet)
        else
            deleteDiet(diet)
    }

    private fun deleteDiet(diet: Diet) {
        doSingleRequest(
            query = { repository.deleteDiet(diet.value) },
            doOnSuccess = {
                builderFilters.deleteDiet(diet)
                _filtersProfile.value = builderFilters.build()
            }
        )
    }

    private fun addDiet(diet: Diet) {
        doSingleRequest(
            query = { repository.addDiet(diet.value) },
            doOnSuccess = {
                builderFilters.addDiet(diet)
                _filtersProfile.value = builderFilters.build()
            }
        )
    }

    private fun getDiets() {
        doSingleRequest(
            query = { repository.getDiets() },
            doOnSuccess = {
                it.forEach { string ->
                    (Diet from string)?.let { diet -> builderFilters.addDiet(diet = diet) }
                }
                _filtersProfile.value = builderFilters.build()
            }
        )
    }

    private fun getIntolerance() {
        doSingleRequest(
            query = { repository.getIntolerance() },
            doOnSuccess = {
                it.forEach { string ->
                    (Intolerance from string)?.let { intolerance ->
                        builderFilters.addIntolerance(intolerance = intolerance)
                    }
                }
                _filtersProfile.value = builderFilters.build()
            }
        )
    }
}
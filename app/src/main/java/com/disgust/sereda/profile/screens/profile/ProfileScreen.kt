package com.disgust.sereda.profile.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.disgust.sereda.profile.screens.profile.interaction.ProfileUIEvent
import com.disgust.sereda.profile.screens.profile.interaction.UserInfoState
import com.disgust.sereda.utils.DoOnInit
import com.disgust.sereda.utils.commonModel.Diet
import com.disgust.sereda.utils.commonModel.Intolerance
import com.disgust.sereda.utils.components.ChipsFilterClickableTest
import com.disgust.sereda.utils.components.ChipsFilterNotClickable
import com.disgust.sereda.utils.components.CustomProgressBar
import com.disgust.sereda.utils.components.FiltersView
import com.disgust.sereda.utils.toImmutableList
import com.disgust.sereda.utils.toImmutableOrEmpty
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ProfileScreen(
    vm: ProfileViewModel
) {
    val userInfoState = vm.userInfoState.collectAsState()
    val filtersProfile = vm.filtersProfile.collectAsState()

    val state = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden, skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()

    val dietsChips = @Composable {
        val setDiet: (Diet, Boolean) -> Unit = remember {
            { diet, isAdd ->
                vm.onUIEvent(ProfileUIEvent.FiltersSetDiet(diet, isAdd))
            }
        }
        ChipsFilterClickableTest(
            selectedChips = filtersProfile.value.dietsList.toImmutableOrEmpty(),
            chips = Diet.values().toImmutableList(),
            setChipState = setDiet
        )
//        ChipsFilterClickable(
//            selectedChips = filtersProfile.value.dietsList.toImmutableOrEmpty(),
//            setChipState = setDiet
//        )
    }

    val intoleranceChips = @Composable {
        val setIntolerance: (Intolerance, Boolean) -> Unit = remember {
            { intolerance, isAdd ->
                vm.onUIEvent(ProfileUIEvent.FiltersSetIntolerance(intolerance, isAdd))
            }
        }
        ChipsFilterClickableTest(
            selectedChips = filtersProfile.value.intolerancesList.toImmutableOrEmpty(),
            chips = Intolerance.values().toImmutableList(),
            setChipState = setIntolerance
        )
//        ChipsFilterClickable(
//            selectedChips = filtersProfile.value.intolerancesList.toImmutableOrEmpty(),
//            setChipState = setIntolerance
//        )
    }

    val dietsChipsSelected = @Composable {
        ChipsFilterNotClickable(chips = filtersProfile.value.dietsList.toImmutableOrEmpty())
    }

    val intoleranceChipsSelected = @Composable {
        ChipsFilterNotClickable(chips = filtersProfile.value.intolerancesList.toImmutableOrEmpty())
    }

    DoOnInit {
        vm.onUIEvent(ProfileUIEvent.StartScreen)
    }


    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = MaterialTheme.shapes.small,
        sheetContent = {
            if (state.isVisible) {
                BackHandler(onBack = {
                    scope.launch { state.hide() }
                })
            }
            FiltersView(dietsChips, intoleranceChips) {
                scope.launch { state.hide() }
            }
        }) {
        Column() {
            when (val userStateValue = userInfoState.value) {
                is UserInfoState.Loading -> {
                    CustomProgressBar()
                }
                is UserInfoState.Success -> {

                    RowButtons(clickable = true) {
                        scope.launch { state.animateTo(ModalBottomSheetValue.HalfExpanded) }
                    }
                    dietsChipsSelected()
                    intoleranceChipsSelected()

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = userStateValue.userInfo.toString())
                        Button(onClick = {
                            vm.onUIEvent(ProfileUIEvent.ButtonSignOutClick)
                        }) {
                            Text(text = "Sign Out")
                        }
                    }
                }
                is UserInfoState.Error -> {
                    RowButtons(clickable = false)
                    Text(text = userStateValue.e.toString())
                }
                is UserInfoState.NotAuth -> {
                    RowButtons(clickable = false)
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = "You need to authorize to see your profile")
                        Button(onClick = {
                            vm.onUIEvent(ProfileUIEvent.ButtonAuthClick)
                        }) {
                            Text(text = "Authorize")
                        }
                    }
                }
                is UserInfoState.Waiting -> {}
            }
        }
    }

}

@Composable
fun RowButtons(clickable: Boolean, onClickSettings: () -> Unit = {}) {
    Row() {
        IconButton(
            onClick = { onClickSettings() },
            modifier = Modifier.size(40.dp),
            enabled = clickable
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Filters"
            )
        }
    }
}
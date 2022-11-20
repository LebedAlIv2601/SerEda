package com.disgust.sereda.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.NavigatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

inline fun <T : Any> ViewModel.doSingleRequest(
    crossinline query: suspend () -> T,
    crossinline doOnLoading: () -> Unit = {},
    crossinline doOnSuccess: (T) -> Unit,
    crossinline doOnError: (Exception) -> Unit = {}
) {
    viewModelScope.launch {
        doOnLoading.invoke()
        try {
            val response = withContext(Dispatchers.IO) { query.invoke() }
            doOnSuccess.invoke(response)
        } catch (e: Exception) {
            doOnError.invoke(e)
        }
    }
}

@Composable
inline fun DoOnInit(
    crossinline func: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        func.invoke()
    }
}

@Composable
inline fun <reified T : NavigatorViewModel> NavigatorViewModelScreen(
    navController: NavHostController,
    viewModel: T = hiltViewModel(),
    screenDrawFun: @Composable (T) -> Unit
) {
    DoOnInit {
        viewModel.instantiateNavController(navController)
    }
    screenDrawFun.invoke(viewModel)
}
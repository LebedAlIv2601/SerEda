package com.disgust.sereda.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.disgust.sereda.utils.base.NavigatorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

fun <T : Any> ViewModel.doSingleRequest(
    query: suspend () -> T,
    doOnLoading: () -> Unit = {},
    doOnSuccess: (T) -> Unit = {},
    doOnError: (Exception) -> Unit = {}
) {
    viewModelScope.launch {
        doOnLoading.invoke()
        try {
            val response = query.invoke()
            doOnSuccess.invoke(response)
        } catch (e: Exception) {
            doOnError.invoke(e)
        }
    }
}

fun <T : Any> ViewModel.subscribeToFlowOnIO(
    flowToCollect: suspend () -> Flow<T>,
    doOnLoading: () -> Unit = {},
    doOnCollect: (T) -> Unit,
    doOnError: (Exception) -> Unit = {}
) {
    viewModelScope.launch {
        doOnLoading.invoke()
        flowToCollect()
            .flowOn(Dispatchers.IO)
            .handleErrors { doOnError(it) }
            .collect {
                doOnCollect(it)
            }
    }
}

fun <T> Flow<T>.handleErrors(
    onError: (Exception) -> Unit
): Flow<T> = flow {
    try {
        collect { value -> emit(value) }
    } catch (e: Exception) {
        onError.invoke(e)
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

fun List<Any?>?.toQueryString(): String {
    return if (this != null) {
        if (size != 0) {
            var query = ""
            forEach { query += "$it," }
            query.substring(0, query.lastIndex).lowercase(Locale.getDefault())
        } else {
            ""
        }
    } else {
        "null"
    }
}

fun <T> List<T>?.toImmutableOrEmpty(): ImmutableList<T> {
    return this?.toImmutable() ?: immutableListOf()
}
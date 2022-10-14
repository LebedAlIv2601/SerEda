package com.disgust.sereda.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T : Any> ViewModel.doSingleRequest(
    query: suspend () -> T,
    doOnLoading: (() -> Unit)? = null,
    doOnSuccess: (T) -> Unit,
    doOnError: (Exception) -> Unit
) {
    viewModelScope.launch {
        doOnLoading?.invoke()
        try {
            val response = withContext(Dispatchers.IO) { query.invoke() }
            doOnSuccess.invoke(response)
        } catch (e: Exception) {
            doOnError.invoke(e)
        }
    }
}
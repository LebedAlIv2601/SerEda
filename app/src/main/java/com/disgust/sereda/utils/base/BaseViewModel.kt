package com.disgust.sereda.utils.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : BaseUIEvent> : ViewModel() {

    abstract fun onEvent(event: T)

}
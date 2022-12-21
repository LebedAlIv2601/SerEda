package com.disgust.sereda.utils.base

import androidx.lifecycle.ViewModel
import com.disgust.sereda.utils.base.navigationDelegate.NavigationHandler
import com.disgust.sereda.utils.base.navigationDelegate.NavigationHandlerImpl

abstract class BaseViewModel<T : BaseUIEvent> :
    ViewModel(),
    NavigationHandler by NavigationHandlerImpl() {

    abstract fun onUIEvent(event: T)

}
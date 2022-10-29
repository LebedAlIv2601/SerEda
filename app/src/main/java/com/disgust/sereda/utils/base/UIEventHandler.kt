package com.disgust.sereda.utils.base

interface UIEventHandler<T : BaseUIEvent> {

    fun onUIEvent(event: T)

}
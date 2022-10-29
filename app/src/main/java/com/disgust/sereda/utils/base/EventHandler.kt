package com.disgust.sereda.utils.base

interface EventHandler<T : BaseUIEvent> {

    fun onEvent(event: T)

}
package com.disgust.sereda.utils.base

open class BaseState {
    inline fun <reified T> doAsStateIfPossible(
        doIfTrue: (T) -> Unit
    ) {
        if (this is T) {
            doIfTrue(this)
        }
    }
}
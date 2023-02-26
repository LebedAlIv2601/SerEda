package com.disgust.sereda.utils

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableList<T>(val items: List<T>)

fun <T> immutableListOf(vararg items: T): ImmutableList<T> {
    val list = if (items.isNotEmpty()) {
        items.toList()
    } else {
        emptyList()
    }
    return ImmutableList(list)
}

fun <T> List<T>.toImmutable(): ImmutableList<T> = ImmutableList(this)
fun <T> Array<T>.toImmutableList(): ImmutableList<T> = ImmutableList(this.asList())
package com.disgust.sereda.utils.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.disgust.sereda.utils.base.BaseState

@Composable
fun <T> PagingList(
    itemsList: List<T>,
    itemComponent: @Composable (T) -> Unit,
    pageSize: Int,
    pagingState: PagingState,
    getData: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {

        itemsIndexed(itemsList) { index, item ->
            pagingState.doAsStateIfPossible<PagingState.Success> {
                if (index == itemsList.size - 1 - pageSize / 3 && !it.isEndReached) {
                    LaunchedEffect(key1 = Unit) {
                        getData(itemsList.size)
                    }
                }
            }
            itemComponent(item)
        }
        when (pagingState) {
            is PagingState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomProgressBar()
                    }
                }
            }
            is PagingState.Error -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Something went wrong")
                    }
                }
            }
            else -> {}
        }
    }
}


sealed class PagingState : BaseState() {
    object Waiting : PagingState()
    object Loading : PagingState()
    object Error : PagingState()
    class Success(val isEndReached: Boolean) : PagingState()
}
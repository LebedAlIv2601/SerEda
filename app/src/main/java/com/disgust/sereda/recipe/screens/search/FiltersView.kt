package com.disgust.sereda.recipe.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.recipe.screens.search.model.IngredientFilter
import com.disgust.sereda.utils.base.BaseChipsEnum

@ExperimentalComposeUiApi
@Composable
fun FiltersView(vararg views: @Composable () -> Unit, onApply: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color.White)
        ) {
            items(views) {
                it()
            }
        }

        ApplyButtonFilter { onApply() }

    }
}

@Composable
fun TopPanelFilter(
    onSearchIngredient: () -> Unit,
    onClose: () -> Unit,
    onDeleteAll: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Text(text = "Фт", fontSize = 14.sp)

        IconButton(onClick = onSearchIngredient) {
            Icon(Icons.Default.Search, contentDescription = "Найти ингридиент")
        }

        IconButton(onClick = onDeleteAll) {
            Icon(Icons.Default.Delete, contentDescription = "Очистить фильтры")
        }

        IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Закрыть фильтры")
        }
    }
}

@Composable
fun ApplyButtonFilter(onApply: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp, 0.dp),
        onClick = onApply
    ) {
        Text(text = "Применить", fontSize = 16.sp)
    }
}

@Composable
fun ListIngredientFilter(
    title: String? = null,
    list: List<IngredientFilter>,
    onDeleteItem: (item: IngredientFilter) -> Unit
) {
    title?.let { Text(text = it) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
    ) {
        for (it in list) {
            ItemIngredient(it, onDeleteItem)
        }
    }
}

@Composable
fun ItemIngredient(item: IngredientFilter, onDeleteItem: (item: IngredientFilter) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(text = "${item.name}  is include: ${item.isInclude}")
        IconButton(onClick = { onDeleteItem(item) }) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить из фильтров")
        }
    }
}

@ExperimentalMaterialApi
@Composable
inline fun <reified T> ChipsFilter(
    title: String? = null,
    selectedChips: List<T>,
    crossinline setChipState: (t: T, isAdd: Boolean) -> Unit,
) where T : Enum<T>, T : BaseChipsEnum {
    title?.let { Text(text = it) }

    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // itemHeight * rowCount + verticalSpacing * (rowCount - 1)
            //сейчас рандомное значение стоит)))
            .padding(5.dp),
        rows = GridCells.Fixed(3),
        content = {
            items(enumValues<T>()) { chip ->
                var state = chip in selectedChips
                FilterChip(
                    modifier = Modifier.wrapContentSize(),
                    selected = state,
                    colors =
                    if (state)
                        ChipDefaults.filterChipColors()
                    else
                        ChipDefaults.outlinedFilterChipColors(),
                    onClick = {
                        state = !state
                        setChipState(chip, state)
                    }) {
                    Text(text = chip.value)
                }
            }
        })
}

@Composable
fun SingleInputFilter(
    label: String? = null,
    title: String? = null,
    value: Int?,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    title?.let { Text(text = it) }
    TextField(
        value = value?.toString() ?: "",
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        label = { label?.let { Text(text = it) } }
    )
}

@Composable
fun MinMaxInputFilter(
    title: String? = null,
    labelMin: String? = null,
    valueMin: Int?,
    onValueChangeMin: (String) -> Unit,
    labelMax: String? = null,
    valueMax: Int?,
    onValueChangeMax: (String) -> Unit,
) {
    title?.let { Text(text = it) }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Box(Modifier.weight(1f)) {
            SingleInputFilter(
                label = labelMin,
                value = valueMin,
                onValueChange = onValueChangeMin
            )
        }
        Box(Modifier.weight(1f)) {
            SingleInputFilter(
                label = labelMax,
                value = valueMax,
                onValueChange = onValueChangeMax
            )
        }
    }
}
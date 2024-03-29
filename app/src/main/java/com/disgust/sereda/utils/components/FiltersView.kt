package com.disgust.sereda.utils.components

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
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.utils.ImmutableList
import com.disgust.sereda.utils.base.BaseChipsEnum
import com.disgust.sereda.utils.commonModel.Diet
import com.disgust.sereda.utils.commonModel.IngredientFilter
import com.disgust.sereda.utils.immutableListOf

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

@Preview
@Composable
fun TopPanelFilterPreview() {
    TopPanelFilter(onSearchIngredient = {}, onClose = {}) {}
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

@Preview
@Composable
fun ApplyButtonPreview() {
    ApplyButtonFilter(onApply = {})
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

@Preview
@Composable
fun ItemIngredientPreview() {
    ItemIngredient(
        item = IngredientFilter(
            id = 0,
            name = "Lemon",
            image = "",
            isInclude = true
        ),
        onDeleteItem = {})
}

@ExperimentalMaterialApi
@Composable
fun <T> ChipsFilterClickableTest(
    title: String? = null,
    chips: ImmutableList<T>,
    selectedChips: ImmutableList<T>,
    setChipState: (t: T, isAdd: Boolean) -> Unit
) where T : Enum<T>, T : BaseChipsEnum {
    title?.let { Text(text = it) }

    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // itemHeight * rowCount + verticalSpacing * (rowCount - 1)
            //сейчас рандомное значение стоит)))
            .padding(5.dp),
        rows = GridCells.Fixed(3)
    ) {
        items(chips.items, key = { it.ordinal }) { chip ->
            var state = selectedChips.items.contains(chip)
            val onChipClick = remember(state) {
                {
                    state = !state
                    setChipState(chip, state)
                }
            }
            FilterChip(
                modifier = Modifier.wrapContentSize(),
                selected = state,
                colors =
                if (state) {
                    ChipDefaults.filterChipColors()
                } else {
                    ChipDefaults.outlinedFilterChipColors()
                },
                onClick = onChipClick
            ) {
                Text(text = chip.value)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
inline fun <reified T> ChipsFilterClickable(
    title: String? = null,
    selectedChips: ImmutableList<T>,
    crossinline setChipState: @DisallowComposableCalls (t: T, isAdd: Boolean) -> Unit
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
                var state = chip in selectedChips.items
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

@ExperimentalMaterialApi
@Preview
@Composable
fun ChipsFilterClickablePreview() {
    ChipsFilterClickable(
        title = "Chips",
        selectedChips = immutableListOf<Diet>(),
        setChipState = { _, _ -> })
}

@ExperimentalMaterialApi
@Composable
fun <T> ChipsFilterNotClickable(
    title: String? = null,
    chips: ImmutableList<T>,
) where T : Enum<T>, T : BaseChipsEnum {
    title?.let { Text(text = it) }

    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // itemHeight * rowCount + verticalSpacing * (rowCount - 1)
            //сейчас рандомное значение стоит)))
            .padding(5.dp),
        rows = GridCells.Fixed(1),
        content = {
            items(chips.items, key = { it.ordinal }) { chip ->
                Chip(
                    modifier = Modifier.wrapContentSize(),
                    enabled = false,
                    colors = ChipDefaults.chipColors(),
                    onClick = {}
                ) {
                    Text(text = chip.value)
                }
            }
        })
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ChipsFilterNotClickablePreview() {
    ChipsFilterNotClickable(
        title = "Chips",
        chips = immutableListOf(Diet.GLUTEN_FREE, Diet.KETOGENIC)
    )
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

@Preview
@Composable
fun SingleInputFilterPreview() {
    SingleInputFilter(
        label = "Ass State",
        title = "State of Ass",
        value = null,
        onValueChange = {})
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

@Preview
@Composable
fun MinMaxInputFilterPreview() {
    MinMaxInputFilter(
        labelMin = "Min Ass",
        labelMax = "Max Ass",
        title = "State of Ass",
        valueMin = null,
        onValueChangeMin = {},
        valueMax = null,
        onValueChangeMax = {},
    )
}
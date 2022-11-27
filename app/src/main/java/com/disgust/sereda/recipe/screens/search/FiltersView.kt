package com.disgust.sereda.recipe.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.recipe.screens.search.model.Diet
import com.disgust.sereda.recipe.screens.search.model.IngredientFilter

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun FiltersView(
    onApply: () -> Unit,
    onSearchIngredient: () -> Unit,
    onClose: () -> Unit,
    onDeleteAll: () -> Unit,
    onDeleteItem: (item: IngredientFilter) -> Unit,
    setDiet: (diet: Diet, isAdd: Boolean) -> Unit,
    ingredientListFilters: List<IngredientFilter>,
    dietsList: List<Diet>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        Text(text = "Diets:")

        LazyHorizontalGrid(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp),
            rows = GridCells.Fixed(3),
            content = {
                items(Diet.values()) { diet ->
                    var state = diet in dietsList
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
                            setDiet(diet, state)
                        }) {
                        Text(text = diet.value)
                    }
                }
            })

        Text(text = "Ingredients:")

        LazyColumn(
            modifier = Modifier
                .weight(3f)
                .fillMaxSize()
                .padding(5.dp)
        ) {
            items(ingredientListFilters) {
                ItemIngredient(it, onDeleteItem)
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp),
            onClick = onApply
        ) {
            Text(text = "Применить", fontSize = 16.sp)
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
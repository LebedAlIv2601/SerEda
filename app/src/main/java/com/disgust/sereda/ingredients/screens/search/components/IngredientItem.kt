package com.disgust.sereda.ingredients.screens.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem


@Composable
fun IngredientItemComponent(ingredient: IngredientItem, onItemClick: (IngredientItem) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { onItemClick(ingredient) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = ingredient.name)
    }
}


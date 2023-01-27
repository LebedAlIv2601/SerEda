package com.disgust.sereda.recipe.screens.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.recipe.screens.info.model.Nutrient

@Composable
fun NutritionGrid(nutrition: List<Nutrient>) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp)
    ) {
        items(nutrition) {
            NutritionItem(nutritionItem = it)
        }
    }
}

@Composable
fun NutritionItem(nutritionItem: Nutrient) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
            .wrapContentWidth()
            .background(Color.White)
            .border(1.dp, Color.Blue)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = nutritionItem.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = "${nutritionItem.amount} ${nutritionItem.unit}")
        Text(text = "${nutritionItem.percent}% from day")
    }
}

@Preview
@Composable
fun NutritionItemPreview() {
    NutritionItem(
        nutritionItem = Nutrient(
            name = "Calories",
            amount = 326.84,
            unit = "kcal",
            percent = 16.34
        )
    )
}
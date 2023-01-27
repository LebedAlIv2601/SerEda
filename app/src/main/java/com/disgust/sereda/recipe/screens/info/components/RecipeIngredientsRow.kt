package com.disgust.sereda.recipe.screens.info.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disgust.sereda.recipe.screens.info.model.Ingredient
import com.disgust.sereda.recipe.screens.info.model.Measures
import com.disgust.sereda.recipe.screens.info.model.Quantity
import com.disgust.sereda.utils.components.ImageIngredientView

@Composable
fun RecipeIngredientsRow(
    list: List<Ingredient>,
    onIngredientClick: (Ingredient) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Ingredients",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(list) {
                RecipeIngredientsRowItem(ingredient = it, onIngredientClick = onIngredientClick)
            }
        }
    }
}

@Composable
fun RecipeIngredientsRowItem(
    ingredient: Ingredient,
    onIngredientClick: (Ingredient) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(8.dp)
            .clickable { onIngredientClick.invoke(ingredient) },
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageIngredientView(
                url = ingredient.imageName,
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Blue, CircleShape)
            )
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 8.dp)
            ) {
                Text(text = ingredient.name)
                Text(text = "${ingredient.measures.metric.amount} ${ingredient.measures.metric.unitShort}")
            }
        }
    }
}

@Preview
@Composable
fun RecipeIngredientsRowItemPreview() {
    RecipeIngredientsRowItem(
        Ingredient(
            id = 9,
            name = "Blueberries",
            imageName = "blueberries.jpg",
            measures = Measures(
                us = Quantity(
                    amount = 4.5,
                    unitShort = "cups",
                    unitLong = "cups"
                ),
                metric = Quantity(
                    amount = 1.065,
                    unitShort = "l",
                    unitLong = "liters"
                ),
            )
        ),
    ) {}
}
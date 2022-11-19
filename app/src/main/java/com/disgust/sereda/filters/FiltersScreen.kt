package com.disgust.sereda.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalComposeUiApi
@Preview
@Composable
fun FiltersScreen(
    //navController: NavHostController,
    //vm: SearchRecipeViewModel = hiltViewModel()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Text(text = "Фильтры", fontSize = 25.sp)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            items(5) { index ->
                Text(text = "Item: $index")
            }
        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
            onClick = { /*TODO*/ }) {
            Text(text = "Применить", fontSize = 16.sp)
        }
    }
}

@Composable
fun ItemIngredient() {
    Row() {

    }
}
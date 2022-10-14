package com.disgust.sereda.utils.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import com.disgust.sereda.ingredients.search.SearchIngredientScreen

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (NavHostController) -> Unit
) {

    @ExperimentalComposeUiApi
    object Screen1 :
        Screen(route = "screen1", screenDrawFun = { Screen1Screen(navController = it) })

    @ExperimentalComposeUiApi
    object SearchIngredient : Screen(route = "search_ingredient", screenDrawFun = {
        SearchIngredientScreen(
            navController = it
        )
    })
}

//TODO: Примеры экранов, переписать на другие
@ExperimentalComposeUiApi
@Composable
fun Screen1Screen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "1 screen")
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        navController.navigate(Screen.SearchIngredient.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "2")
            }
        }
    }
}
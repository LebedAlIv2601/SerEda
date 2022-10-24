package com.disgust.sereda.utils.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.disgust.sereda.ingredients.screens.info.IngredientInfoScreen
import com.disgust.sereda.ingredients.screens.search.SearchIngredientScreen

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (NavHostController, NavBackStackEntry) -> Unit
) {

    @ExperimentalComposeUiApi
    object Screen1 :
        Screen(route = "screen1", screenDrawFun = { navController, _ ->
            Screen1Screen(navController = navController)
        })

    @ExperimentalComposeUiApi
    object SearchIngredient :
        Screen(route = "search_ingredient", screenDrawFun = { navController, _ ->
            SearchIngredientScreen(
                navController = navController
            )
        })

    object IngredientInfo : Screen(
        route = "ingredient_info/{ingredientId}",
        arguments = listOf(navArgument("ingredientId") { type = NavType.IntType }),
        screenDrawFun = { navController, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("ingredientId")
            IngredientInfoScreen(navController = navController, ingredientId = id ?: 0)
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
package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.disgust.sereda.auth.code.CodeEnterScreen
import com.disgust.sereda.auth.phone.PhoneEnterScreen
import com.disgust.sereda.ingredients.screens.info.IngredientInfoScreen
import com.disgust.sereda.ingredients.screens.search.SearchIngredientScreen
import com.disgust.sereda.splash.SplashScreen
import com.disgust.sereda.recipe.screens.info.RecipeInfoScreen

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (NavHostController, NavBackStackEntry) -> Unit
) {

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object Splash : Screen(
        route = "splash",
        screenDrawFun = { navController, _ ->
            SplashScreen(navController = navController)
        }
    )

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

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object PhoneEnter : Screen(
        route = "phone_enter",
        screenDrawFun = { navController, _ ->
            PhoneEnterScreen(navController = navController)
        }
    )

    @ExperimentalComposeUiApi
    object CodeEnter : Screen(
        route = "code_enter",
        screenDrawFun = { navController, _ ->
            CodeEnterScreen(navController = navController)
        }
    )

    object RecipeInfo : Screen(
        route = "recipe_info/{recipeId}",
        arguments = listOf(navArgument("recipeId") { type = NavType.IntType }),
        screenDrawFun = { navController, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("recipeId")
            RecipeInfoScreen(navController = navController, recipeId = id ?: 0)
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

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        navController.navigateWithArguments(
                            Screen.RecipeInfo.route,
                            mapOf("recipeId" to "716429")
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "3")
            }
        }
    }
}
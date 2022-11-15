package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RootNavGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        createDestination(Screen.Screen1, navController)
        createDestination(Screen.SearchIngredient, navController)
        createDestination(Screen.IngredientInfo, navController)
        createDestination(Screen.PhoneEnter, navController)
        createDestination(Screen.CodeEnter, navController)
        createDestination(Screen.Splash, navController)
        createDestination(Screen.RecipeInfo, navController)
        createDestination(Screen.SearchRecipe, navController)
    }
}

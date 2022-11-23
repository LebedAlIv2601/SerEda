package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import com.disgust.sereda.auth.googleAuth.GoogleAuthScreen
import com.disgust.sereda.ingredients.screens.info.IngredientInfoScreen
import com.disgust.sereda.ingredients.screens.search.SearchIngredientScreen
import com.disgust.sereda.profile.screens.profile.ProfileScreen
import com.disgust.sereda.recipe.commonModel.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.info.RecipeInfoScreen
import com.disgust.sereda.recipe.screens.search.SearchRecipeScreen
import com.disgust.sereda.splash.SplashScreen

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (NavHostController, NavBackStackEntry) -> Unit
) {

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object Splash : Screen(
        route = "splash",
        screenDrawFun = { navController, _ ->
            SplashScreen(navController = navController)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object SearchIngredient :
        Screen(route = "search_ingredient", screenDrawFun = { navController, _ ->
            SearchIngredientScreen(
                navController = navController
            )
        })

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    object IngredientInfo : Screen(
        route = "ingredient_info/{ingredientId}/{ingredientName}",
        arguments = listOf(
            navArgument("ingredientId") { type = NavType.IntType },
            navArgument("ingredientName") { type = NavType.StringType }
        ),
        screenDrawFun = { navController, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("ingredientId")
            val name = navBackStackEntry.arguments?.getString("ingredientName")
            IngredientInfoScreen(
                navController = navController,
                ingredientId = id ?: 0,
                ingredientName = name ?: ""
            )
        })

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object GoogleAuth : Screen(
        route = "google_auth",
        screenDrawFun = { navController, _ ->
            GoogleAuthScreen(navController = navController)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object Profile : Screen(
        route = "profile",
        screenDrawFun = { navController, _ ->
            ProfileScreen(navController = navController)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object SearchRecipe :
        Screen(route = "search_recipe", screenDrawFun = { navController, _ ->
            SearchRecipeScreen(
                navController = navController
            )
        })

    object RecipeInfo : Screen(
        route = "recipe_info/{recipeId}/{favoriteState}",
        arguments = listOf(
            navArgument("recipeId") { type = NavType.IntType },
            navArgument("favoriteState") { type = NavType.IntType }),
        screenDrawFun = { navController, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("recipeId")
            val state = navBackStackEntry.arguments?.getInt("favoriteState")
            RecipeInfoScreen(
                navController = navController,
                recipeId = id ?: 0,
                favoriteState = state ?: RecipeFavoriteState.NOT_FAVORITE.ordinal
            )
        })
}
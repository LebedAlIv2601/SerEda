package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import com.disgust.sereda.auth.googleAuth.GoogleAuthScreen
import com.disgust.sereda.auth.googleAuth.GoogleAuthViewModel
import com.disgust.sereda.ingredients.screens.info.IngredientInfoScreen
import com.disgust.sereda.ingredients.screens.info.IngredientInfoViewModel
import com.disgust.sereda.ingredients.screens.search.SearchIngredientScreen
import com.disgust.sereda.ingredients.screens.search.SearchIngredientViewModel
import com.disgust.sereda.profile.screens.profile.ProfileScreen
import com.disgust.sereda.profile.screens.profile.ProfileViewModel
import com.disgust.sereda.recipe.commonModel.RecipeFavoriteState
import com.disgust.sereda.recipe.screens.info.RecipeInfoScreen
import com.disgust.sereda.recipe.screens.info.RecipeInfoViewModel
import com.disgust.sereda.recipe.screens.search.SearchRecipeScreen
import com.disgust.sereda.recipe.screens.search.SearchRecipeViewModel
import com.disgust.sereda.splash.SplashScreen
import com.disgust.sereda.splash.SplashViewModel
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.disgust.sereda.utils.firebase.FirebaseAuthHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
sealed class Screen<T : NavigatorViewModel>(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (T, NavBackStackEntry) -> Unit
) {

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object Splash : Screen<SplashViewModel>(
        route = "splash",
        screenDrawFun = { vm, _ ->
            SplashScreen(vm = vm)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object SearchIngredient :
        Screen<SearchIngredientViewModel>(route = "search_ingredient", screenDrawFun = { vm, _ ->
            SearchIngredientScreen(vm = vm)
        })

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    object IngredientInfo : Screen<IngredientInfoViewModel>(
        route = "ingredient_info/{ingredientId}/{ingredientName}",
        arguments = listOf(
            navArgument("ingredientId") { type = NavType.IntType },
            navArgument("ingredientName") { type = NavType.StringType }
        ),
        screenDrawFun = { vm, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("ingredientId")
            val name = navBackStackEntry.arguments?.getString("ingredientName")
            IngredientInfoScreen(
                vm = vm,
                ingredientId = id ?: 0,
                ingredientName = name ?: ""
            )
        })

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    object GoogleAuth : Screen<GoogleAuthViewModel>(
        route = "google_auth",
        screenDrawFun = { vm, _ ->
            GoogleAuthScreen(vm = vm)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object Profile : Screen<ProfileViewModel>(
        route = "profile",
        screenDrawFun = { vm, _ ->
            ProfileScreen(vm = vm)
        }
    )

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object SearchRecipe :
        Screen<SearchRecipeViewModel>(route = "search_recipe", screenDrawFun = { vm, _ ->
            SearchRecipeScreen(vm = vm)
        })

    object RecipeInfo : Screen<RecipeInfoViewModel>(
        route = "recipe_info/{recipeId}/{favoriteState}",
        arguments = listOf(
            navArgument("recipeId") { type = NavType.IntType },
            navArgument("favoriteState") { type = NavType.IntType }),
        screenDrawFun = { vm, navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("recipeId")
            val state = navBackStackEntry.arguments?.getInt("favoriteState")
            RecipeInfoScreen(
                recipeId = id ?: 0,
                favoriteState = state ?: RecipeFavoriteState.NOT_FAVORITE.ordinal,
                vm = vm
            )
        })
}
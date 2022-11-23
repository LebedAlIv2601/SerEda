package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    object Screen1 :
        Screen<Screen1ViewModel>(route = "screen1", screenDrawFun = { vm, _ ->
            Screen1Screen(vm = vm)
        })

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    object SearchIngredient :
        Screen<SearchIngredientViewModel>(route = "search_ingredient", screenDrawFun = { vm, _ ->
            SearchIngredientScreen(vm = vm)
        })

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

//TODO: Примеры экранов, переписать на другие

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltViewModel
class Screen1ViewModel @Inject constructor(firebaseAuthHelper: FirebaseAuthHelper) :
    NavigatorViewModel() {
    fun navigateToProfile() {
        navigate(Screen.Profile.route)
    }

    fun navigateToSearchRecipe() {
        navigate(Screen.SearchRecipe.route)
    }

    fun navigateToSearchIngredient() {
        navigate(Screen.SearchIngredient.route)
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun Screen1Screen(
    vm: Screen1ViewModel
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
                        vm.navigateToSearchIngredient()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Search ingredient")
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        vm.navigateToSearchRecipe()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Search recipe")
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        vm.navigateToProfile()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Profile")
            }
        }
    }
}
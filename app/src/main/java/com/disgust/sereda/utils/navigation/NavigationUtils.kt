package com.disgust.sereda.utils.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.disgust.sereda.utils.NavigatorViewModelScreen
import com.disgust.sereda.utils.base.NavigatorViewModel
import com.google.accompanist.navigation.animation.composable

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
inline fun <reified T : NavigatorViewModel> NavGraphBuilder.createDestination(
    screen: Screen<T>,
    navController: NavHostController
) {
    composable(
        route = screen.route,
        arguments = screen.arguments,
        deepLinks = screen.deepLinks,
        enterTransition = { slideInHorizontally(initialOffsetX = { 2000 }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -2000 }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -2000 }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 2000 }) }
    ) { navBackStackEntry ->
        NavigatorViewModelScreen<T>(navController = navController) { vm ->
            screen.screenDrawFun(vm, navBackStackEntry)
        }
    }
}

fun NavHostController.navigateWithArguments(
    destination: String,
    arguments: Map<String, String>
) {
    var destinationWithArgs = destination
    for (argument in arguments) {
        destinationWithArgs = destinationWithArgs
            .replace("{${argument.key}}", argument.value)
    }
    navigate(destinationWithArgs)
}

fun NavHostController.navigateWithClearBackStack(
    destination: String
) {
    navigate(destination) {
        popUpTo(0)
    }
}

package com.disgust.sereda.utils.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable

@ExperimentalAnimationApi
fun NavGraphBuilder.createDestination(screen: Screen, navController: NavHostController) {
    composable(
        route = screen.route,
        arguments = screen.arguments,
        deepLinks = screen.deepLinks,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        screen.screenDrawFun(navController, it)
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

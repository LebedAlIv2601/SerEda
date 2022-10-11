package com.disgust.sereda.utils

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.disgust.sereda.navigation.Screen

fun NavGraphBuilder.createDestination(screen: Screen, navController: NavHostController){
    composable(
        route = screen.route,
        arguments = screen.arguments,
        deepLinks = screen.deepLinks
    ){
        screen.screenDrawFun(navController)
    }
}

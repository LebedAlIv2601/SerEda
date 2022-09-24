package com.disgust.sereda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.disgust.sereda.utils.createGraph

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Graph.AppGraph.route
    ) {
        createGraph(Graph.AppGraph, navController)
    }
}

package com.disgust.sereda.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.disgust.sereda.utils.createAppGraph

@Composable
fun AppNavGraph(
    navController: NavHostController,
    customBackHandlingEnabled: MutableState<Boolean>
) {
    NavHost(
        navController = navController,
        startDestination = Graph.MainGraph.route
    ) {
        createAppGraph(
            graph = Graph.MainGraph,
            navController = navController,
            backHandlingEnabled = customBackHandlingEnabled
        )
        createAppGraph(
            graph = Graph.SecondGraph,
            navController = navController,
            backHandlingEnabled = customBackHandlingEnabled
        )
    }
}
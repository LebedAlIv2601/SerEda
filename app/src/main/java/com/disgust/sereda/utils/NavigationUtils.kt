package com.disgust.sereda.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.composable
import com.disgust.sereda.navigation.Graph

inline fun NavGraphBuilder.appGraphComposable(
    route: String,
    navController: NavHostController,
    backHandlingEnabled: MutableState<Boolean>,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route, arguments, deepLinks) {
        content.invoke(it)
        backHandlingEnabled.value = getAppBackHandlingEnabled(navController)
    }
}

fun NavGraphBuilder.createAppGraph(
    graph: Graph,
    navController: NavHostController,
    backHandlingEnabled: MutableState<Boolean>
) {
    navigation(startDestination = graph.startDestination, route = graph.route) {
        graph.screens.forEach { screen ->
            appGraphComposable(
                route = screen.route,
                navController = navController,
                backHandlingEnabled = backHandlingEnabled,
                arguments = screen.arguments,
                deepLinks = screen.deepLinks
            ) {
                screen.screenDrawFun.invoke(navController)
            }
        }

        graph.innerGraphs.forEach { innerGraph ->
            createGraph(
                graph = innerGraph,
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.createGraph(
    graph: Graph,
    navController: NavHostController
) {
    navigation(startDestination = graph.startDestination, route = graph.route) {
        graph.screens.forEach { screen ->
            composable(
                route = screen.route,
                arguments = screen.arguments,
                deepLinks = screen.deepLinks
            ) {
                screen.screenDrawFun.invoke(navController)
            }
        }

        graph.innerGraphs.forEach { innerGraph ->
            createGraph(
                graph = innerGraph,
                navController = navController
            )
        }
    }
}

fun getAppBackHandlingEnabled(navController: NavHostController): Boolean {
    fun NavGraph.isRoot(): Boolean = parent != null && parent?.parent == null
    val currentNavGraph = navController.currentBackStackEntry?.destination?.parent
    val prevNavGraph = navController.previousBackStackEntry?.destination?.parent
    return (currentNavGraph != prevNavGraph
            && currentNavGraph?.isRoot() == true && prevNavGraph?.isRoot() == true)
}

fun bottomNavigationItemClick(
    graph: Graph,
    navController: NavHostController,
    bottomNavSelectedItem: MutableState<Graph>
) {
    bottomNavSelectedItem.value = graph
    navController.navigate(graph.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
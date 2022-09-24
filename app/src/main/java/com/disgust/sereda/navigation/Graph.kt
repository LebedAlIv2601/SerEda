package com.disgust.sereda.navigation

import com.disgust.sereda.navigation.Screen.*

sealed class Graph(
    val route: String,
    val screens: List<Screen> = emptyList(),
    val innerGraphs: List<Graph> = emptyList(),
    val startDestination: String
) {
    //Примеры графов
    object AppGraph : Graph(
        route = "app_graph",
        screens = listOf(ScreenHome),
        startDestination = ScreenHome.route
    )

    object MainGraph : Graph(
        route = "main_graph",
        screens = listOf(Screen1, Screen2),
        startDestination = Screen1.route
    )

    object SecondGraph : Graph(
        route = "second_graph",
        screens = listOf(Screen3, Screen4),
        startDestination = Screen3.route
    )
}
package com.disgust.sereda.utils.base.navigationDelegate

import androidx.navigation.NavHostController

interface NavigationHandler {

    var navController: NavHostController?

    fun instantiateNavController(navController: NavHostController)

    fun navigate(destination: String)

    fun navigateWithArguments(destination: String, arguments: Map<String, String>)

    fun navigateWithClearBackStack(destination: String)

    fun navigateUp()

    fun popBackStack()

    fun popBackStack(destination: String, isInclusive: Boolean)
}
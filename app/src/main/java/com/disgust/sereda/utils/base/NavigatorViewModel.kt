package com.disgust.sereda.utils.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.disgust.sereda.utils.navigation.navigateWithArguments
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack

abstract class NavigatorViewModel : ViewModel() {

    private var navController: NavHostController? = null

    fun instantiateNavController(navController: NavHostController) {
        if (this.navController == null) {
            this.navController = navController
        }
    }

    fun navigate(destination: String) {
        navController?.navigate(destination)
    }

    fun navigateWithArguments(destination: String, arguments: Map<String, String>) {
        navController?.navigateWithArguments(destination, arguments)
    }

    fun navigateWithClearBackStack(destination: String) {
        navController?.navigateWithClearBackStack(destination)
    }

    fun navigateUp() {
        navController?.navigateUp()
    }

    fun popBackStack() {
        navController?.popBackStack()
    }

    fun popBackStack(destination: String, isInclusive: Boolean) {
        navController?.popBackStack(destination, isInclusive)
    }
}
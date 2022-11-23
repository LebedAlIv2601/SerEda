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

    protected fun navigate(destination: String) {
        navController?.navigate(destination)
    }

    protected fun navigateWithArguments(destination: String, arguments: Map<String, String>) {
        navController?.navigateWithArguments(destination, arguments)
    }

    protected fun navigateWithClearBackStack(destination: String) {
        navController?.navigateWithClearBackStack(destination)
    }

    protected fun navigateUp() {
        navController?.navigateUp()
    }

    protected fun popBackStack() {
        navController?.popBackStack()
    }

    protected fun popBackStack(destination: String, isInclusive: Boolean) {
        navController?.popBackStack(destination, isInclusive)
    }
}
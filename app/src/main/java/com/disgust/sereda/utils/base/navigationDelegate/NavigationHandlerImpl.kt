package com.disgust.sereda.utils.base.navigationDelegate

import androidx.navigation.NavHostController
import com.disgust.sereda.utils.navigation.navigateWithArguments
import com.disgust.sereda.utils.navigation.navigateWithClearBackStack

class NavigationHandlerImpl : NavigationHandler {
    override var navController: NavHostController? = null

    override fun instantiateNavController(navController: NavHostController) {
        if (this.navController == null) {
            this.navController = navController
        }
    }

    override fun navigate(destination: String) {
        navController?.navigate(destination)
    }

    override fun navigateWithArguments(destination: String, arguments: Map<String, String>) {
        navController?.navigateWithArguments(destination, arguments)
    }

    override fun navigateWithClearBackStack(destination: String) {
        navController?.navigateWithClearBackStack(destination)
    }

    override fun navigateUp() {
        navController?.navigateUp()
    }

    override fun popBackStack() {
        navController?.popBackStack()
    }

    override fun popBackStack(destination: String, isInclusive: Boolean) {
        navController?.popBackStack(destination, isInclusive)
    }
}
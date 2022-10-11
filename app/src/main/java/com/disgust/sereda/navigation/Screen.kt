package com.disgust.sereda.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val screenDrawFun: @Composable (NavHostController) -> Unit
) {

    object Screen1 :
        Screen(route = "screen1", screenDrawFun = { Screen1Screen(navController = it) })

    object Screen2 : Screen(route = "screen2", screenDrawFun = { Screen2Screen() })
    object Screen3 :
        Screen(route = "screen3", screenDrawFun = { Screen3Screen(navController = it) })

    object Screen4 : Screen(route = "screen4", screenDrawFun = { Screen4Screen() })
}

//TODO: Примеры экранов, переписать на другие
@Composable
fun Screen1Screen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "1 screen")
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        navController.navigate(Screen.Screen2.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "2")
            }
        }
    }
}

@Composable
fun Screen2Screen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "2 screen")
    }
}

@Composable
fun Screen3Screen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(text = "3 screen")
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        navController.navigate(Screen.Screen4.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "4")
            }
        }
    }
}

@Composable
fun Screen4Screen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "4 screen")
    }
}
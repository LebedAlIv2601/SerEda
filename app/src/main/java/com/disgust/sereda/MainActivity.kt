package com.disgust.sereda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController
import com.disgust.sereda.navigation.RootNavGraph
import com.disgust.sereda.ui.theme.SerEdaTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SerEdaTheme {
                val navController = rememberAnimatedNavController()
                RootNavGraph(navController = navController)
            }
        }
    }
}
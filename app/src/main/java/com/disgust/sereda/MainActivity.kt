package com.disgust.sereda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.disgust.sereda.ui.theme.SerEdaTheme
import com.disgust.sereda.utils.navigation.RootNavGraph
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
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
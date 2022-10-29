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

    // Не использовать, опасная штука с возможной утечкой памяти.
    // Используется ТОЛЬКО для Firebase, потому что ему зачем-то нужно
    // передавать активность при регистрации через номер телефона
    companion object {
        private var mainActivity: MainActivity? = null

        fun getInstance(): MainActivity? = mainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            SerEdaTheme {
                val navController = rememberAnimatedNavController()
                RootNavGraph(navController = navController)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        mainActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity = null
    }
}
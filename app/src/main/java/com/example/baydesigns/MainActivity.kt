package com.example.baydesigns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baydesigns.screens.ColorRecognitionScreen
import com.example.baydesigns.screens.HomeScreen
import com.example.baydesigns.screens.ThreeDToolsScreen
import com.example.baydesigns.ui.theme.BayDesignsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BayDesignsTheme {
                val viewModel: SharedViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController, viewModel)
                    }
                    composable("3dTools") {
                        ThreeDToolsScreen(navController, viewModel)
                    }
                    composable("colorRecognition") {
                        ColorRecognitionScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}
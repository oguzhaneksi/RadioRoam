package com.radioroam.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.radioroam.android.ui.navigation.Screen
import com.radioroam.android.ui.screens.FavoritesScreen
import com.radioroam.android.ui.screens.HomeScreen
import com.radioroam.android.ui.theme.AppTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                AppTheme {
                    // A surface container using the 'background' color from the theme
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.title
                        ) {
                            composable(route = Screen.Home.title) {
                                HomeScreen(
                                    navController = navController
                                )
                            }
                            composable(route = Screen.Favorites.title) {
                                FavoritesScreen(
                                    navController = navController
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
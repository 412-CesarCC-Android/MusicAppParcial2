package com.example.cesarcazaresmusicapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cesarcazaresmusicapp.ui.screens.DetailScreen
import com.example.cesarcazaresmusicapp.ui.screens.HomeScreen
import com.example.cesarcazaresmusicapp.ui.viewmodel.MusicViewModel
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Detail(val id: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MusicViewModel = viewModel()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                viewModel = viewModel,
                onAlbumClick = { id ->
                    navController.navigate(Detail(id))
                }
            )
        }
        composable<Detail> { backStackEntry ->
            val detail: Detail = backStackEntry.toRoute()
            DetailScreen(
                viewModel = viewModel,
                albumId = detail.id,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

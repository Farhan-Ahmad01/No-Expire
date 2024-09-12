package com.example.noexpire.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noexpire.screens.AboutScreen
import com.example.noexpire.screens.AddUpdateProduct
import com.example.noexpire.screens.HomeScreen
import com.example.noexpire.viewModels.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", builder = {
        
        composable("home") {
            HomeScreen(navController, homeViewModel = homeViewModel)
        }
        
        composable("addUpdate") {
            AddUpdateProduct(navController ,homeViewModel = homeViewModel)
        }

        composable("about") {
            AboutScreen(navController = navController)
        }

    })

}
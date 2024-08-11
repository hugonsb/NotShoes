package com.ahpp.notshoes.navigation.deslogado

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.view.viewsDeslogado.LoginScreen
import com.ahpp.notshoes.view.viewsDeslogado.RegistroScreen
import com.ahpp.notshoes.view.viewsLogado.BottomNavBar

@Composable
fun NavManagerDeslogado() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LoginScreen(navController)
        }
        composable(route = "registro") {
            RegistroScreen(navController)
        }
        composable(route = "bottomNavBar") {
            BottomNavBar(navController)
        }
    }
}
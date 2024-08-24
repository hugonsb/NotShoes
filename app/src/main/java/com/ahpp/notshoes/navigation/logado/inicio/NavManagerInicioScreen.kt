package com.ahpp.notshoes.navigation.logado.inicio

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahpp.notshoes.view.screensReutilizaveis.ResultadosScreen
import com.ahpp.notshoes.view.viewsLogado.viewsInicio.InicioScreen

@Composable
fun NavManagerInicioScreen(navBarController: NavHostController) {
    val navControllerInicio = rememberNavController()
    NavHost(navController = navControllerInicio, startDestination = "inicioScreen") {

        composable(route = "inicioScreen") {
            InicioScreen(navControllerInicio, navBarController)
        }

        composable(route = "resultadosScreen/{valorBusca}/{tipoBusca}/{fromScreen}",
            arguments = listOf(
                navArgument("valorBusca") { type = NavType.StringType },
                navArgument("tipoBusca") { type = NavType.StringType },
                navArgument("fromScreen") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val valorBusca = backStackEntry.arguments?.getString("valorBusca")
            val tipoBusca = backStackEntry.arguments?.getString("tipoBusca")
            val fromScreen = backStackEntry.arguments?.getString("fromScreen")
            ResultadosScreen(
                navControllerInicio,
                valorBusca.toString(),
                tipoBusca.toString(),
                fromScreen.toString()
            )
        }
    }
}
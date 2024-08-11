package com.ahpp.notshoes.navigation.categoria

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahpp.notshoes.view.screensReutilizaveis.ResultadosScreen
import com.ahpp.notshoes.view.viewsLogado.viewsCategoria.CategoriaScreen

@Composable
fun NavManagerCategoriaScreen() {
    val navControllerCategoria = rememberNavController()
    NavHost(navController = navControllerCategoria, startDestination = "categoriaScreen") {

        composable(route = "categoriaScreen") {
            CategoriaScreen(navControllerCategoria)
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
            ResultadosScreen(navControllerCategoria, valorBusca.toString(), tipoBusca.toString(), fromScreen.toString())
        }
    }
}
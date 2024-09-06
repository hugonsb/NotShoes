package com.ahpp.notshoes.navigation.logado.listaDesejos

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahpp.notshoes.view.screensReutilizaveis.ProdutoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsListaDesejos.ListaDesejoscreen

@Composable
fun NavManagerListaDesejosSCreen(){
    val navControllerListaDesejos = rememberNavController()
    NavHost(navController = navControllerListaDesejos, startDestination = "listaDesejosScreen"){

        composable(route = "listaDesejosScreen"){
            ListaDesejoscreen(navControllerListaDesejos)
        }

        composable(
            route = "produtoScreen/{produtoId}",
            arguments = listOf(
                navArgument("produtoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val produtoId = backStackEntry.arguments?.getInt("produtoId")!!
            ProdutoScreen(navController = navControllerListaDesejos, produtoId = produtoId)
        }
    }
}
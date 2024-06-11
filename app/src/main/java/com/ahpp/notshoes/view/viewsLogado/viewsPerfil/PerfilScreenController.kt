package com.ahpp.notshoes.view.viewsLogado.viewsPerfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.EnderecosScreenController
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsPedidos.PedidosScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.SeusDadosScreenController
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSobreApp.SobreAppScreen

@Composable
fun PerfilScreenController(navControllerLogin: NavController) {
    val navControllerPerfil = rememberNavController()
    NavHost(navController = navControllerPerfil, startDestination = "perfilScreen") {

        composable(route = "perfilScreen") {
            PerfilScreen(navControllerLogin, navControllerPerfil)
        }

        composable(route = "pedidosScreen") {
            PedidosScreen(navControllerPerfil)
        }
        composable(route = "seusDadosScreen") {
            SeusDadosScreenController(navControllerPerfil)
        }
        composable(route = "enderecosScreen") {
            EnderecosScreenController(navControllerPerfil)
        }
        composable(route = "sobreAppScreen") {
            SobreAppScreen(navControllerPerfil)
        }
    }
}
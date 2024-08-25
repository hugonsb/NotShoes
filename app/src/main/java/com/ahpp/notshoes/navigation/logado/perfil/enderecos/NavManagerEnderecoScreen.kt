package com.ahpp.notshoes.navigation.logado.perfil.enderecos

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.CadastrarEnderecoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.EditarEnderecoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.EnderecosScreen

@Composable
fun NavManagerEnderecoScreen(navControllerPerfil: NavController) {
    val navControllerEnderecos = rememberNavController()
    NavHost(navController = navControllerEnderecos, startDestination = "enderecosScreen") {
        composable(route = "enderecosScreen") {
            EnderecosScreen(navControllerPerfil, navControllerEnderecos)
        }
        composable(route = "cadastrarEnderecoScreen") {
            CadastrarEnderecoScreen(navControllerEnderecos)
        }
        composable(route = "editarEnderecoScreen") {
            EditarEnderecoScreen(navControllerEnderecos)
        }
    }
}
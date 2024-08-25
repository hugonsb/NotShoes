package com.ahpp.notshoes.navigation.logado.perfil.seusDados

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.AtualizarDadosPessoaisScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.AtualizarEmailScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.AtualizarSenhaScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.SeusDadosScreen

@Composable
fun NavManagerSeusDadosScreen(navControllerPerfil: NavController) {
    val navControllerSeusDados = rememberNavController()
    NavHost(navController = navControllerSeusDados, startDestination = "seusDadosScreen") {
        composable(route = "seusDadosScreen") {
            SeusDadosScreen(navControllerPerfil, navControllerSeusDados)
        }
        composable(route = "atualizarDadosPessoaisScreen") {
            AtualizarDadosPessoaisScreen(navControllerSeusDados)
        }
        composable(route = "atualizarEmailScreen") {
            AtualizarEmailScreen(navControllerSeusDados)
        }
        composable(route = "atualizarSenhaScreen") {
            AtualizarSenhaScreen(navControllerSeusDados)
        }
    }
}
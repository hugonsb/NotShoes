package com.ahpp.notshoes.navigation.logado.carrinho

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.view.viewsLogado.viewsCarrinho.CarrinhoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsCarrinho.CompraFinalizadaScreen
import com.ahpp.notshoes.view.viewsLogado.viewsCarrinho.FinalizarPedidoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.CadastrarEnderecoScreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados.AtualizarDadosPessoaisScreen

@Composable
fun NavManagerCarrinhoScreen() {
    val navControllerCarrinho = rememberNavController()
    NavHost(navController = navControllerCarrinho, startDestination = "carrinhoScreen") {
        composable(route = "carrinhoScreen") {
            CarrinhoScreen(navControllerCarrinho)
        }
        composable(route = "finalizarPedidoScreen") {
            FinalizarPedidoScreen(navControllerCarrinho)
        }
        composable(route = "compraFinalizadaScreen") {
            CompraFinalizadaScreen(navControllerCarrinho)
        }
        composable(route = "atualizarDadosPessoaisScreen") {
            AtualizarDadosPessoaisScreen(navControllerCarrinho)
        }
        composable(route = "cadastrarEnderecoScreen") {
            CadastrarEnderecoScreen(navControllerCarrinho)
        }
    }
}
package com.ahpp.notshoes.view.viewsLogado

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.navigation.categoria.NavManagerCategoriaScreen
import com.ahpp.notshoes.navigation.inicio.NavManagerInicioScreen
import com.ahpp.notshoes.ui.theme.azulClaro
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.view.viewsLogado.viewsCarrinho.CarrinhoScreenController
import com.ahpp.notshoes.view.viewsLogado.viewsListaDesejos.ListaDeDesejoscreen
import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.PerfilScreenController

lateinit var produtoSelecionado: Produto

@Composable
fun BottomNavBar(navControllerLogin: NavController) {

    val navBarController = rememberNavController()
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Categorias,
        BottomNavItem.Carrinho,
        BottomNavItem.ListaDesejos,
        BottomNavItem.Perfil
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(50.dp),
                containerColor = azulClaro,
                tonalElevation = 3.dp,
            ) {
                val navBackStackEntry by navBarController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White,
                            indicatorColor = azulEscuro
                        ),
                        //label = { Text(screen.label, fontSize = 8.sp) },
                        alwaysShowLabel = false,
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navBarController.navigate(screen.route) {
                                popUpTo(screen.route) {
                                    inclusive = true
                                }
                                // // evitar abrir novamente a mesma tela ao reselecionar mesmo item
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navBarController,
            startDestination = BottomNavItem.Inicio.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Inicio.route) { NavManagerInicioScreen(navBarController) }
            composable(BottomNavItem.Categorias.route) { NavManagerCategoriaScreen() }
            composable(BottomNavItem.Carrinho.route) { CarrinhoScreenController() }
            composable(BottomNavItem.ListaDesejos.route) { ListaDeDesejoscreen() }
            composable(BottomNavItem.Perfil.route) { PerfilScreenController(navControllerLogin) }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Inicio : BottomNavItem("inicioScreen", Icons.Default.Home, "Inicio")
    data object Categorias :
        BottomNavItem("categoriasScreen", Icons.AutoMirrored.Filled.List, "Categorias")

    data object Carrinho : BottomNavItem("carrinhoScreen", Icons.Default.ShoppingCart, "Carrinho")
    data object ListaDesejos :
        BottomNavItem("listaDesejosScreen", Icons.Default.Favorite, "Lista de desejos")

    data object Perfil : BottomNavItem("perfilScreen", Icons.Default.Person, "Perfil")
}
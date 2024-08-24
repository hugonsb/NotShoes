package com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.data.endereco.getEnderecos
import com.ahpp.notshoes.model.Endereco
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.util.cards.CardEndereco
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.view.screensReutilizaveis.SemConexaoScreen
import com.ahpp.notshoes.constantes.clienteLogado

lateinit var enderecoSelecionado: Endereco

@Composable
fun EnderecosScreen(navControllerPerfil: NavController, navControllerEnderecos: NavController) {

    val ctx = LocalContext.current
    var internetCheker by remember { mutableStateOf(possuiConexao(ctx)) }

    Column(modifier = Modifier.fillMaxSize()) {

        if (!internetCheker) {
            SemConexaoScreen(onBackPressed = {
                internetCheker = possuiConexao(ctx)
            })
        } else {
            var enderecosList by remember { mutableStateOf(emptyList<Endereco>()) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                enderecosList = getEnderecos(clienteLogado.idCliente)
                isLoading = false
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White
                        )
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(azulEscuro)
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .size(45.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                if (navControllerPerfil.canGoBack) {
                                    navControllerPerfil.popBackStack("perfilScreen", false)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.White),
                            elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                            Image(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.toque_para_voltar_description),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = stringResource(R.string.seus_enderecos),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {

                        if (enderecosList.isNotEmpty()) {

                            // Separar o endereço principal dos outros endereços e colocar no topo da lista
                            val enderecoPrincipal =
                                enderecosList.find { it.idEndereco == clienteLogado.idEnderecoPrincipal }
                            val outrosEnderecos =
                                enderecosList.filter { it.idEndereco != clienteLogado.idEnderecoPrincipal }
                            val listaEnderecosOrganizada =
                                listOfNotNull(enderecoPrincipal) + outrosEnderecos

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp, end = 10.dp)
                            ) {
                                LazyColumn {
                                    items(items = listaEnderecosOrganizada) { endereco ->
                                        CardEndereco(onClickEditarEndereco = {
                                            navControllerEnderecos.navigate("editarEnderecoScreen") {
                                                launchSingleTop = true
                                            }
                                            enderecoSelecionado = endereco
                                        },
                                            endereco,
                                            //atualizar a lista de endereços na tela após remover algum endereço
                                            onRemoveEndereco = { removedEndereco ->
                                                enderecosList =
                                                    enderecosList.filter { it.idEndereco != removedEndereco.idEndereco }
                                            })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(70.dp))
                                    }
                                }
                                FloatingActionButton(
                                    onClick = {
                                        navControllerEnderecos.navigate("cadastrarEnderecoScreen") {
                                            launchSingleTop = true
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .align(Alignment.BottomEnd),
                                    containerColor = azulEscuro,
                                    contentColor = Color.White,
                                    shape = FloatingActionButtonDefaults.largeShape
                                ) {
                                    Icon(Icons.Filled.Add,
                                        stringResource(R.string.cadastrar_endereco))
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 45.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.nenhum_endereco_cadastrado),
                                    fontSize = 25.sp,
                                    color = azulEscuro
                                )

                                Spacer(modifier = Modifier.height(30.dp))

                                ElevatedButton(
                                    onClick = {
                                        navControllerEnderecos.navigate("cadastrarEnderecoScreen") {
                                            launchSingleTop = true
                                        }
                                    },
                                    modifier = Modifier
                                        .width(230.dp)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = azulEscuro
                                    ),
                                    shape = RoundedCornerShape(5.dp),
                                ) {
                                    Text(
                                        text = stringResource(R.string.cadastrar_endereco),
                                        fontSize = 15.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                        }

                    }

                }
            }
        }
    }
}
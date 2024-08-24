package com.ahpp.notshoes.view.viewsLogado.viewsCarrinho

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.barraValoresCarrinho
import com.ahpp.notshoes.ui.theme.textoValorEconomizado
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.util.cards.CardItemCarrinho
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.view.screensReutilizaveis.SemConexaoScreen
import com.ahpp.notshoes.viewModel.logado.carrinho.CarrinhoScreenViewModel
import java.text.NumberFormat

@Composable
fun CarrinhoScreen(
    navControllerCarrinho: NavController,
    carrinhoScreenViewModel: CarrinhoScreenViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        carrinhoScreenViewModel.atualizarCarrinho()
    }

    val ctx = LocalContext.current
    var internetCheker by remember { mutableStateOf(possuiConexao(ctx)) }
    val uiState by carrinhoScreenViewModel.carrinhoScreenState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!internetCheker) {
        SemConexaoScreen(onBackPressed = {
            internetCheker = possuiConexao(ctx)
        })
    } else {
        CarrinhoContent(navControllerCarrinho, carrinhoScreenViewModel, ctx)
    }
}

@Composable
fun CarrinhoContent(
    navControllerCarrinho: NavController,
    carrinhoScreenViewModel: CarrinhoScreenViewModel,
    ctx: Context
) {
    // manter a posicao do scroll ao voltar pra tela
    val listState = rememberLazyListState()
    val uiState by carrinhoScreenViewModel.carrinhoScreenState.collectAsState()

    val itensList = uiState.itensList
    val valorTotal = uiState.valorTotal
    val valorTotalComDesconto = uiState.valorTotalComDesconto
    val combinedList = uiState.combinedList

    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                openDialog.value = false
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            title = {
                Text(
                    text = stringResource(R.string.dados_incompletos),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    stringResource(R.string.complete_seus_dados_para_prosseguir),
                    textAlign = TextAlign.Start
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        navControllerCarrinho.navigate("atualizarDadosPessoaisScreen") {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(stringResource(R.string.confirmar), color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancelar), color = Color.Black)
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.carrinho),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(
                    Color.White
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                if (combinedList.isNotEmpty()) {
                    LazyColumn(state = listState) {
                        items(
                            items = combinedList,
                            key = { it.first.idProduto }) { (item, produto) ->
                            CardItemCarrinho(
                                produto = produto,
                                item = item,
                                onRemoveProduto = { produtoToRemove ->
                                    carrinhoScreenViewModel.removerProduto(
                                        ctx,
                                        produtoToRemove
                                    ) { carrinhoScreenViewModel.atualizarCarrinho() }
                                },
                                adicionarUnidade = { itemToAdd ->
                                    carrinhoScreenViewModel.adicionarUnidade(
                                        ctx,
                                        itemToAdd
                                    ) { carrinhoScreenViewModel.atualizarCarrinho() }
                                },
                                removerUnidade = { itemToRemove ->
                                    carrinhoScreenViewModel.removerUnidade(
                                        ctx,
                                        itemToRemove
                                    ) { carrinhoScreenViewModel.atualizarCarrinho() }
                                }
                            )
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
                            text = stringResource(R.string.seu_carrinho_esta_vazio),
                            fontSize = 25.sp,
                            color = azulEscuro
                        )
                    }
                }
            }
        }

        if (itensList.isNotEmpty()) {

            val localeBR = java.util.Locale("pt", "BR")
            val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(barraValoresCarrinho)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .width(270.dp),
                        text = "Total: ${numberFormat.format(valorTotal)}",
                        fontSize = 10.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Text(
                        modifier = Modifier
                            .width(270.dp),
                        text = "Com desconto: ${numberFormat.format(valorTotalComDesconto)}",
                        fontSize = 15.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Text(
                        modifier = Modifier
                            .width(270.dp),
                        text = "Você irá economizar: ${numberFormat.format(valorTotal - valorTotalComDesconto)}",
                        fontSize = 11.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = textoValorEconomizado
                    )
                }
                ElevatedButton(
                    onClick = {
                        if (itensList.isNotEmpty() && clienteLogado.cpf != "" && clienteLogado.telefoneContato != "") {
                            navControllerCarrinho.navigate("finalizarPedidoScreen") {
                                launchSingleTop = true
                            }
                        } else {
                            openDialog.value = true
                        }
                    },
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = verde)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,

                        ) {
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(R.string.finalizar),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Icon(
                            tint = Color.White.copy(alpha = 0.9f),
                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                            contentDescription = stringResource(R.string.finalizar_compra_description),
                        )
                    }
                }
            }
        }
    }
}
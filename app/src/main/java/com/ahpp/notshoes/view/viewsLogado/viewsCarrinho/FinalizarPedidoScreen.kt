package com.ahpp.notshoes.view.viewsLogado.viewsCarrinho

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.carrinho.FinalizarPedido
import com.ahpp.notshoes.model.Venda
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.branco
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.util.RadioButtonButtonPersonalizado
import com.ahpp.notshoes.util.cards.CardEnderecoCarrinho
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.view.screensReutilizaveis.SemConexaoScreen
import com.ahpp.notshoes.viewModel.logado.carrinho.CarrinhoScreenViewModel
import java.io.IOException
import java.text.NumberFormat
import java.time.LocalDate

@Composable
fun FinalizarPedidoScreen(
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
                .background(
                    branco
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!internetCheker) {
        SemConexaoScreen(onBackPressed = {
            internetCheker = possuiConexao(ctx)
        })
    } else {
        FinalizarPedidoContent(carrinhoScreenViewModel, navControllerCarrinho, ctx)
    }
}

@Composable
fun FinalizarPedidoContent(
    carrinhoScreenViewModel: CarrinhoScreenViewModel,
    navControllerCarrinho: NavController,
    ctx: Context
) {
    val uiState by carrinhoScreenViewModel.carrinhoScreenState.collectAsState()
    val itemList = uiState.itensList
    val detalhesPedido = uiState.detalhesPedido
    val valorTotalPedido = uiState.valorTotalComDesconto
    val enderecoParaEntrega = uiState.enderecoParaEntrega
    val enderecosList = uiState.enderecosList

    val scrollState = rememberScrollState()
    var enabledButton by remember { mutableStateOf(true) }
    val localeBR = java.util.Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

    var tipoEntrega by remember { mutableStateOf("Expressa") }
    var tipoPagamento by remember { mutableStateOf("Pix") }
    var valorFrete by remember { mutableDoubleStateOf(if (tipoEntrega == "Expressa") 30.0 else 0.0) }
    var expandedEnderecos by remember { mutableStateOf(false) }

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
            Button(
                modifier = Modifier
                    .size(45.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    if (navControllerCarrinho.canGoBack) {
                        navControllerCarrinho.popBackStack("carrinhoScreen", false)
                    }
                },
                colors = ButtonDefaults.buttonColors(branco),
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
                text = stringResource(R.string.finalizar_pedido),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        if (expandedEnderecos) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp),

                ) {
                Text(
                    text = stringResource(R.string.selecione_um_endereco),
                    fontSize = 20.sp
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                items(
                    enderecosList,
                    key = { it.idEndereco }) { endereco ->
                    Card(
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            Color.Black,
                            Color.Black,
                            Color.Black
                        ),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable(enabled = true, onClick = {
                                carrinhoScreenViewModel.setEnderecoParaEntrega(endereco)
                                expandedEnderecos = false
                            }),
                        elevation = CardDefaults.cardElevation(4.dp),
                    ) {
                        CardEnderecoCarrinho(endereco)
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ElevatedButton(
                            onClick = {
                                navControllerCarrinho.navigate("cadastrarEnderecoScreen") {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(40.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = azulEscuro
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.cadastrar_outro_endereco),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                style = TextStyle(
                                    Color.White
                                )
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)

            ) {
                if (enderecosList.isNotEmpty() && enderecoParaEntrega != null) {
                    Card(
                        shape = RoundedCornerShape(5.dp),
                        colors = CardColors(
                            containerColor = Color.White,
                            Color.Black,
                            Color.Black,
                            Color.Black
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                    ) {
                        CardEnderecoCarrinho(enderecoParaEntrega)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ElevatedButton(
                            onClick = {
                                expandedEnderecos = true
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(40.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = azulEscuro
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.usar_outro_endereco),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                style = TextStyle(
                                    Color.White
                                )
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            text = stringResource(R.string.nenhum_endereco_cadastrado),
                            fontSize = 25.sp,
                            color = Color.Red
                        )

                        ElevatedButton(
                            onClick = {
                                navControllerCarrinho.navigate("cadastrarEnderecoScreen") {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .width(230.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = azulEscuro
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.adicionar_endereco),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                style = TextStyle(
                                    Color.White
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.baseline_directions_car_24),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "  " + stringResource(R.string.selecione_o_tipo_de_entrega),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    RadioButtonButtonPersonalizado(
                        text = "Expressa",
                        isSelected = tipoEntrega == "Expressa",
                        onClick = {
                            tipoEntrega = "Expressa"
                            valorFrete = 30.0
                        }
                    )
                    RadioButtonButtonPersonalizado(
                        text = "Normal",
                        isSelected = tipoEntrega == "Normal",
                        onClick = {
                            tipoEntrega = "Normal"
                            valorFrete = 0.0
                        }
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 30.dp, start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.baseline_attach_money_24),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "  " + stringResource(R.string.forma_de_pagamento),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    RadioButtonButtonPersonalizado(
                        text = "Pix",
                        isSelected = tipoPagamento == "Pix",
                        onClick = { tipoPagamento = "Pix" }
                    )
                    RadioButtonButtonPersonalizado(
                        text = "Cartão de crédito",
                        isSelected = tipoPagamento == "Cartão de crédito",
                        onClick = { tipoPagamento = "Cartão de crédito" }
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 30.dp, start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.baseline_checklist_24),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = "  " + stringResource(R.string.valor_total),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = detalhesPedido,
                            fontSize = 10.sp,
                        )
                    }

                }
            }
        }
        if (!expandedEnderecos) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.frete) + " ",
                    fontSize = 15.sp,
                )
                Text(
                    text = if (tipoEntrega == "Expressa") numberFormat.format(valorFrete) else "Grátis",
                    fontSize = 15.sp,
                    color = if (tipoEntrega == "Expressa") Color.Black else Color.Green,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.valor_total) + ": ",
                    fontSize = 15.sp,
                )
                Text(
                    text = numberFormat.format(valorTotalPedido + valorFrete),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            ElevatedButton(
                onClick = {
                    if (enderecoParaEntrega != null) {
                        if (possuiConexao(ctx)) {
                            //desativar o botao para evitar compra dupla
                            enabledButton = false
                            val resumoCompra =
                                "$detalhesPedido\n\nForma de pagamento: $tipoPagamento\nTipo de entrega: $tipoEntrega\n\nValor Total: ${
                                    numberFormat.format(
                                        valorTotalPedido + valorFrete
                                    )
                                }"

                            val venda = Venda(
                                dataPedido = LocalDate.now(),
                                status = "Em processamento",
                                detalhesPedido = resumoCompra,
                                formaPagamento = tipoPagamento,
                                idCarrinho = clienteLogado.idCarrinho
                            )

                            val finalizarPedido = FinalizarPedido(venda, itemList)

                            finalizarPedido.sendFinalizarPedido(
                                object : FinalizarPedido.Callback {
                                    override fun onSuccess(code: String) {
                                        if (code == "1") {
                                            Handler(Looper.getMainLooper()).post {
                                                carrinhoScreenViewModel.atualizarCarrinho()
                                                navControllerCarrinho.navigate("compraFinalizadaScreen") {
                                                    launchSingleTop = true
                                                }
                                            }
                                        } else {
                                            Handler(Looper.getMainLooper()).post {
                                                Toast.makeText(
                                                    ctx,
                                                    R.string.erro_limpe_seu_carrinho_e_tente_novamente,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                    override fun onFailure(e: IOException) {
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                R.string.erro_rede,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        enabledButton = true
                                    }
                                }
                            )
                        }
                    } else {
                        Toast.makeText(
                            ctx,
                            R.string.selecione_um_endere_o_para_entrega,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                enabled = enabledButton,
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    Text(
                        text = stringResource(R.string.confirmar_pedido),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }

}
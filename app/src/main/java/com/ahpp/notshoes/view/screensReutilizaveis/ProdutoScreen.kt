package com.ahpp.notshoes.view.screensReutilizaveis

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahpp.notshoes.R
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.branco
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.viewModel.screensReutilizaveis.ProdutoScreenViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat

@Composable
fun ProdutoScreen(
    navController: NavController,
    produtoId: Int,
    produtoScreenViewModel: ProdutoScreenViewModel = koinViewModel {
        parametersOf(produtoId)
    }
) {
    val ctx = LocalContext.current
    val uiState by produtoScreenViewModel.produtoScreenState.collectAsState()

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        val produto = uiState.produto
        val favorito = uiState.favorito
        produto?.let {
            ProdutoContent(produto, favorito, produtoId, navController, produtoScreenViewModel, ctx)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ProdutoContent(
    produto: Produto,
    favorito: Boolean,
    produtoId: Int,
    navController: NavController,
    produtoScreenViewModel: ProdutoScreenViewModel,
    ctx: Context
) {
    val localeBR = java.util.Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(produto.imagemProduto)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build()
    )
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(azulEscuro),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier
                    .size(65.dp)
                    .padding(top = 10.dp, start = 10.dp, bottom = 10.dp, end = 10.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    if (navController.canGoBack)
                        navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.toque_para_voltar_description),
                    modifier = Modifier.size(30.dp)
                )
            }
            Button(
                modifier = Modifier
                    .size(65.dp)
                    .padding(top = 10.dp, start = 10.dp, bottom = 10.dp, end = 10.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    produtoScreenViewModel.adicionarListaDesejos(produtoId, ctx)
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(if (!favorito) R.drawable.baseline_favorite_border_24 else R.drawable.baseline_favorite_filled_24),
                    contentDescription = stringResource(id = R.string.adicionar_favoritos_description),
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        //a coluna abaixo tem o conteudo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(top = 10.dp)
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Image(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }

                else -> {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }
            Column {
                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    text = produto.nomeProduto,
                    fontSize = 25.sp,
                )

                if (produto.emOferta) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .height(50.dp)
                            .background(verde)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(35.dp),
                                painter = painterResource(id = R.drawable.baseline_access_alarm_24),
                                contentDescription = null,
                            )
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.produto_oferta_aproveite),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(color = Color.Black)
                                )
                                Text(
                                    text = stringResource(id = R.string.enquanto_durar_estoque),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row {
                            Text(
                                text = numberFormat.format(produto.preco.toDouble()),
                                textDecoration = TextDecoration.LineThrough,
                                style = TextStyle(Color.Gray),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val percentualDesconto = 100 * produto.desconto.toDouble()
                            val percentualDescontoFormated =
                                String.format("%.0f", percentualDesconto)
                            Text(
                                text = "-$percentualDescontoFormated%",
                                style = TextStyle(verde),
                                fontSize = 15.sp
                            )
                        }

                        val valorComDesconto =
                            produto.preco.toDouble() - ((produto.preco.toDouble() * produto.desconto.toDouble()))

                        Row(modifier = Modifier, verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = numberFormat.format(valorComDesconto),
                                fontWeight = FontWeight.Bold,
                                fontSize = 35.sp
                            )
                            Text(
                                text = " " + stringResource(id = R.string.a_vista),
                                fontSize = 20.sp
                            )
                        }

                        if (produto.estoqueProduto > 0) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.em_estoque),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.estoque_esgotado),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.Red
                            )
                        }
                    }

                    ElevatedButton(
                        onClick = {
                            produtoScreenViewModel.adicionarProdutoCarrinho(
                                produtoId,
                                ctx
                            )
                        },
                        modifier = Modifier
                            .width(95.dp)
                            .height(55.dp)
                            .align(Alignment.CenterVertically),
                        contentPadding = PaddingValues(0.dp),
                        enabled = produto.estoqueProduto > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = verde)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                            contentDescription = stringResource(id = R.string.adicionar_carrinho_description),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    text = "Detalhes do produto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.DarkGray
                )

                Text(
                    modifier = Modifier.padding(
                        top = 5.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                    text = produto.descricao,
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
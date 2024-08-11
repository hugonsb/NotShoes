package com.ahpp.notshoes.view.viewsLogado.viewsListaDesejos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.ahpp.notshoes.R
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.util.cards.CardListaDesejos
import com.ahpp.notshoes.util.funcoes.conexao.possuiConexao
import com.ahpp.notshoes.view.screensReutilizaveis.ProdutoScreen
import com.ahpp.notshoes.view.screensReutilizaveis.SemConexaoScreen
import com.ahpp.notshoes.viewModel.logado.listaDesejos.ListaDesejosViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListaDeDesejoscreen(listaDesejosViewModel: ListaDesejosViewModel = koinViewModel<ListaDesejosViewModel>()) {

    val ctx = LocalContext.current

    var internetCheker by remember { mutableStateOf(possuiConexao(ctx)) }

    //clickedProduto é usado para monitorar a tela de produto selecionado
    //ela se torna true quando um produto é clicado lá em CardListaDesejos()
    //e false quando clica em voltar na tela de produto selecionado

    var clickedProduto by remember { mutableStateOf(false) }

    if (!internetCheker) {
        SemConexaoScreen(onBackPressed = {
            internetCheker = possuiConexao(ctx)
        })
    } else if (clickedProduto) {
        ProdutoScreen(onBackPressed = {
            clickedProduto = false
            internetCheker = possuiConexao(ctx)
            listaDesejosViewModel.atualizarListaDesejos()
        })
    } else {
        ListaDesejosContent(onclickedProduto = { clickedProduto = true }, listaDesejosViewModel)
    }
}

@Composable
fun ListaDesejosContent(
    onclickedProduto: () -> Unit,
    listaDesejosViewModel: ListaDesejosViewModel
) {

    val ctx = LocalContext.current

    val uiState by listaDesejosViewModel.listaDesejosState.collectAsState()

    val produtosList = uiState.produtosList

    // manter a posicao do scroll ao voltar pra tela
    val listState = rememberLazyListState()

    if (uiState.isLoading) {
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
                .background(Color.White)
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
                    .background(azulEscuro),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    text = stringResource(R.string.lista_de_desejos), fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {

                if (produtosList.isNotEmpty()) {
                    LazyColumn(
                        state = listState
                    ) {
                        items(items = produtosList, key = { it.idProduto }) { produto ->
                            CardListaDesejos(onClickProduto = { onclickedProduto() },
                                produto,
                                onRemoveProduct = {
                                    listaDesejosViewModel.removerProdutoListaDesejos(
                                        produto.idProduto,
                                        ctx
                                    )
                                })
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
                            text = stringResource(R.string.sua_lista_de_desejos_esta_vazia),
                            fontSize = 25.sp,
                            color = azulEscuro
                        )
                    }
                }
            }
        }
    }
}
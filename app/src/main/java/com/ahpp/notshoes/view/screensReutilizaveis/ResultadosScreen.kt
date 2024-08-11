package com.ahpp.notshoes.view.screensReutilizaveis

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.ColorsExposedDropdownMenu
import com.ahpp.notshoes.constantes.FiltrosList.coresList
import com.ahpp.notshoes.constantes.FiltrosList.precosList
import com.ahpp.notshoes.constantes.FiltrosList.tamanhosList
import com.ahpp.notshoes.constantes.FiltrosList.tipoOrdenacaoList
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.branco
import com.ahpp.notshoes.util.cards.CardResultados
import com.ahpp.notshoes.view.viewsLogado.produtoSelecionado
import com.ahpp.notshoes.viewModel.screensReutilizaveis.ResultadosScreenViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(
    navController: NavController,
    valorBusca: String?,
    tipoBusca: String?,
    fromScreen: String?,
    resultadosScreenViewModel: ResultadosScreenViewModel = koinViewModel {
        parametersOf(tipoBusca, valorBusca)
    }
) {
    val ctx = LocalContext.current

    val uiState by resultadosScreenViewModel.resultadosScreenState.collectAsState()
    var clickedProduto by remember { mutableStateOf(false) }

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
    } else if (clickedProduto) {
        ProdutoScreen(onBackPressed = { clickedProduto = false },
            favoritado = uiState.favoritos[produtoSelecionado.idProduto] ?: "0",
            onFavoritoClick = { favoritado ->
                resultadosScreenViewModel.adicionarListaDesejos(favoritado, produtoSelecionado)
            })
    } else {
        val produtosList = uiState.produtosList
        val favoritos = uiState.favoritos

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
            Column(
                modifier = Modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(azulEscuro),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(10.dp),
                        contentPadding = PaddingValues(0.dp),
                        onClick = {
                            when (fromScreen) {
                                "inicioScreen" -> navController.navigate("inicioScreen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                }

                                "categoriaScreen" -> navController.navigate("categoriaScreen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                }
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
                        modifier = Modifier
                            .width(220.dp),
                        text = valorBusca.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (uiState.expandedFiltro) {
                            Button(
                                modifier = Modifier
                                    .size(65.dp)
                                    .padding(10.dp),
                                contentPadding = PaddingValues(0.dp),
                                onClick = {
                                    resultadosScreenViewModel.resetFiltros(ctx)
                                },
                                colors = ButtonDefaults.buttonColors(branco),
                                elevation = ButtonDefaults.buttonElevation(10.dp)
                            ) {
                                Image(
                                    painterResource(id = R.drawable.baseline_refresh_24),
                                    contentDescription = stringResource(id = R.string.limpar_filtros_description),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .size(65.dp)
                                .padding(10.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { resultadosScreenViewModel.setExpandedFiltro() },
                            colors = if (uiState.expandedFiltro) ButtonDefaults.buttonColors(Color.Gray) else ButtonDefaults.buttonColors(
                                Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                            Image(
                                painterResource(R.drawable.baseline_filter_list_24),
                                contentDescription = stringResource(id = R.string.exibir_filtros_description),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                if (uiState.expandedFiltro) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(azulEscuro)
                    ) {
                        Row {
                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 5.dp)
                                    .weight(1f),
                                expanded = uiState.expandedPrecosList,
                                onExpandedChange = {
                                    resultadosScreenViewModel.setExpandedPrecosList(
                                        it
                                    )
                                },
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = uiState.preco,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = uiState.expandedPrecosList
                                        )
                                    },
                                    colors = ColorsExposedDropdownMenu.customColorsExposedDropdownMenu()
                                )
                                ExposedDropdownMenu(
                                    expanded = uiState.expandedPrecosList,
                                    onDismissRequest = {
                                        resultadosScreenViewModel.setExpandedPrecosList(
                                            false
                                        )
                                    },
                                ) {
                                    precosList.forEach { precoSelecionadoFiltro ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    precoSelecionadoFiltro,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            onClick = {
                                                resultadosScreenViewModel.setPreco(
                                                    precoSelecionadoFiltro
                                                )
                                                resultadosScreenViewModel.setExpandedPrecosList(
                                                    false
                                                )
                                                if (valorBusca != null && tipoBusca != null) {
                                                    resultadosScreenViewModel.buscarProduto(
                                                        ctx,
                                                        valorBusca,
                                                        tipoBusca
                                                    )
                                                }
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 5.dp)
                                    .weight(1f),
                                expanded = uiState.expandedTamanhosList,
                                onExpandedChange = {
                                    resultadosScreenViewModel.setExpandedTamanhosList(
                                        it
                                    )
                                },
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = uiState.tamanho,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = uiState.expandedTamanhosList
                                        )
                                    },
                                    colors = ColorsExposedDropdownMenu.customColorsExposedDropdownMenu()
                                )
                                ExposedDropdownMenu(
                                    expanded = uiState.expandedTamanhosList,
                                    onDismissRequest = {
                                        resultadosScreenViewModel.setExpandedTamanhosList(
                                            false
                                        )
                                    },
                                ) {
                                    tamanhosList.forEach { tamanhoSelecionadoFiltro ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    tamanhoSelecionadoFiltro,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            onClick = {
                                                resultadosScreenViewModel.setTamanho(
                                                    tamanhoSelecionadoFiltro
                                                )
                                                resultadosScreenViewModel.setExpandedTamanhosList(
                                                    false
                                                )
                                                if (valorBusca != null && tipoBusca != null) {
                                                    resultadosScreenViewModel.buscarProduto(
                                                        ctx,
                                                        valorBusca,
                                                        tipoBusca
                                                    )
                                                }
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 5.dp)
                                    .weight(1f),
                                expanded = uiState.expandedCoresList,
                                onExpandedChange = {
                                    resultadosScreenViewModel.setExpandedCoresList(
                                        it
                                    )
                                },
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = uiState.cor,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = uiState.expandedCoresList
                                        )
                                    },
                                    colors = ColorsExposedDropdownMenu.customColorsExposedDropdownMenu()
                                )
                                ExposedDropdownMenu(
                                    expanded = uiState.expandedCoresList,
                                    onDismissRequest = {
                                        resultadosScreenViewModel.setExpandedCoresList(
                                            false
                                        )
                                    },
                                ) {
                                    coresList.forEach { corSelecionadaFiltro ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    corSelecionadaFiltro,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            onClick = {
                                                resultadosScreenViewModel.setCor(
                                                    corSelecionadaFiltro
                                                )
                                                resultadosScreenViewModel.setExpandedCoresList(false)
                                                if (valorBusca != null && tipoBusca != null) {
                                                    resultadosScreenViewModel.buscarProduto(
                                                        ctx,
                                                        valorBusca,
                                                        tipoBusca
                                                    )
                                                }
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }

                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 5.dp)
                                    .weight(1f),
                                expanded = uiState.expandedtipoOrdenacaoList,
                                onExpandedChange = {
                                    resultadosScreenViewModel.setExpandedtipoOrdenacaoList(
                                        it
                                    )
                                },
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = uiState.tipoOrdenacao,
                                    onValueChange = {},
                                    readOnly = true,
                                    singleLine = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = uiState.expandedtipoOrdenacaoList
                                        )
                                    },
                                    colors = ColorsExposedDropdownMenu.customColorsExposedDropdownMenu()
                                )
                                ExposedDropdownMenu(
                                    expanded = uiState.expandedtipoOrdenacaoList,
                                    onDismissRequest = {
                                        resultadosScreenViewModel.setExpandedtipoOrdenacaoList(
                                            false
                                        )
                                    },
                                ) {
                                    tipoOrdenacaoList.forEach { ordenacaoSelecionada ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    ordenacaoSelecionada,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            },
                                            onClick = {
                                                resultadosScreenViewModel.setTipoOrdenacao(
                                                    ordenacaoSelecionada
                                                )
                                                resultadosScreenViewModel.setExpandedtipoOrdenacaoList(
                                                    false
                                                )
                                                if (valorBusca != null && tipoBusca != null) {
                                                    resultadosScreenViewModel.buscarProduto(
                                                        ctx,
                                                        valorBusca,
                                                        tipoBusca
                                                    )
                                                }
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                if (produtosList.isNotEmpty()) {
                    LazyColumn(state = listState) {
                        items(items = produtosList) { produto ->
                            CardResultados(
                                onClickProduto = {
                                    produtoSelecionado = produto
                                    clickedProduto = true
                                },
                                produto = produto,
                                favoritado = favoritos[produto.idProduto] ?: "0",
                                onFavoritoClick = { favoritado ->
                                    resultadosScreenViewModel.adicionarListaDesejos(
                                        favoritado,
                                        produto
                                    )
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
                            text = stringResource(id = R.string.nenhum_produto_encontrado),
                            fontSize = 28.sp,
                            color = azulEscuro
                        )
                    }
                }
            }

        }
    }
}
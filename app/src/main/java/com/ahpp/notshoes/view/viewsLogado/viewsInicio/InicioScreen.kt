package com.ahpp.notshoes.view.viewsLogado.viewsInicio

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ahpp.notshoes.R
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.ui.theme.backgroundBarraPesquisa
import com.ahpp.notshoes.ui.theme.branco
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.view.screensReutilizaveis.SemConexaoScreen
import com.ahpp.notshoes.viewModel.logado.inicio.PromocoesInicioScreenViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

@Composable
fun InicioScreen(
    navControllerInicio: NavHostController, navBarController: NavHostController,
    promocoesInicioScreenViewModel: PromocoesInicioScreenViewModel = koinViewModel<PromocoesInicioScreenViewModel>()
) {
    val uiState by promocoesInicioScreenViewModel.promocoesInicioScreenState.collectAsState()
    var internetCheker by remember { mutableStateOf(false) }

    // manter a posicao do scroll ao voltar pra tela
    val scrollState = rememberScrollState()
    val ctx = LocalContext.current
    internetCheker = possuiConexao(ctx)

    //funcionalidade "toque novamente pra sair"
    //dessa forma só atinte a tela inicio
    var backPressedOnce = false
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Fecha o aplicativo ao clicar em voltar em menos de 2s
                if (backPressedOnce) {
                    (ctx as? Activity)?.finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(
                        ctx,
                        R.string.toque_em_voltar_sair,
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed(
                        { backPressedOnce = false },
                        2000
                    )
                }
            }
        }
    }

    // callback para interceptar o evento de voltar
    DisposableEffect(onBackPressedDispatcher) {
        onBackPressedDispatcher?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }

    if (!internetCheker) {
        SemConexaoScreen(onBackPressed = {
            internetCheker = possuiConexao(ctx)
            if (internetCheker) promocoesInicioScreenViewModel.getPromocoes()
        })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SearchBar(navControllerInicio)
            PagerDescontos(navControllerInicio)
            PagerFiltroValores(navControllerInicio)
            FiltrosTelaInicial(navControllerInicio, navBarController)
            Promocoes(uiState.ofertas, navControllerInicio)
            NavegarPorMarcas(navControllerInicio)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navControllerInicio: NavHostController) {
    var text by remember { mutableStateOf("") }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        colors = SearchBarDefaults.colors(backgroundBarraPesquisa),
        query = text,
        onQueryChange = { text = it },
        onSearch = {
            if (text.isNotEmpty()) {
                navControllerInicio.navigate("resultadosScreen/$text/nome/inicioScreen") {
                    launchSingleTop = true
                }
            }
        },
        active = false,
        onActiveChange = { },
        placeholder = {
            Text(
                text = stringResource(id = R.string.busque_um_produto),
                fontSize = 14.sp
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        navControllerInicio.navigate("resultadosScreen/$text/nome/inicioScreen") {
                            launchSingleTop = true
                        }
                    }
                },
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.buscar_produtos_description),
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    ) {
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerDescontos(navControllerInicio: NavHostController) {
    val images = listOf(
        painterResource(R.drawable.img_banner_nike_day),
        painterResource(R.drawable.img_banner_adidas),
        painterResource(R.drawable.img_banner_olymp),
        painterResource(R.drawable.img_banner_air_jordan),
    )

    val itensList = listOf(
        "nike",
        "tênis adidas",
        "tênis olympikus",
        "air jordan 1 mid"
    )

    val pagerState =
        androidx.compose.foundation.pager.rememberPagerState(pageCount = { images.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { currentPage ->
                Card(
                    modifier = Modifier
                        .clickable(enabled = true, onClick = {
                            navControllerInicio.navigate("resultadosScreen/${itensList[currentPage]}/nome/inicioScreen") {
                                launchSingleTop = true
                            }
                        }),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .height(245.dp),
                        painter = images[currentPage],
                        contentDescription = ""
                    )
                }
            }
        }
        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
        )

    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = 5.dp)
            //coloquei essa altura aqui fixa porque o IndicatorDots vai alterando de tamanho,
            // e isso ficava mexendo no tamanho geral da tela
            .height(18.dp)
    ) {
        repeat(pageCount) {
            IndicatorDots(isSelected = it == currentPage, modifier = modifier)
        }
    }
}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if (isSelected) 12.dp else 10.dp, label = "")
    Box(
        modifier = modifier
            .padding(2.dp)
            .size(size.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.Black else Color.Gray)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerFiltroValores(navControllerInicio: NavHostController) {

    val images = listOf(
        painterResource(R.drawable.img_valor_ate_199),
        painterResource(R.drawable.img_valor_200_ate_399),
        painterResource(R.drawable.img_valor_400_ate_599),
        painterResource(R.drawable.img_valor_acima_600)
    )

    val filtrosPrecoList = listOf(
        "ate R$199",
        "de R$200 ate R$399",
        "de R$400 ate R$599",
        "acima de R$600"
    )

    val pagerState =
        androidx.compose.foundation.pager.rememberPagerState(pageCount = { images.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.padding(top = 10.dp),
    ) {
        Box {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 35.dp)
            ) { currentPage ->
                Card(
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {
                                navControllerInicio.navigate("resultadosScreen/${filtrosPrecoList[currentPage]}/preco/inicioScreen") {
                                    launchSingleTop = true
                                }
                            }),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .width(325.dp),
                        painter = images[currentPage],
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}

@Composable
fun FiltrosTelaInicial(
    navControllerInicio: NavHostController,
    navBarController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 0.dp, end = 0.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.size(60.dp), contentPadding = PaddingValues(0.dp),
                onClick = {
                    navControllerInicio.navigate("resultadosScreen/Camisa Básica/categoria/inicioScreen") {
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_categoria_camisa_basica),
                    contentDescription = stringResource(id = R.string.acessar_categoria_camisa_basica),
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.camisa_basica),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 10.sp,
                color = Color.DarkGray,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.size(60.dp), contentPadding = PaddingValues(0.dp),
                onClick = {
                    navControllerInicio.navigate("resultadosScreen/Regata/categoria/inicioScreen") {
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_categoria_regata),
                    contentDescription = stringResource(id = R.string.acessar_categoria_regata),
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.regata),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 10.sp,
                color = Color.DarkGray,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.size(60.dp), contentPadding = PaddingValues(0.dp),
                onClick = {
                    navControllerInicio.navigate("resultadosScreen/Calça/categoria/inicioScreen") {
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                ),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_categoria_calca),
                    contentDescription = stringResource(id = R.string.acessar_categoria_calca),
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.calca),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 10.sp,
                color = Color.DarkGray,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.size(60.dp), contentPadding = PaddingValues(0.dp),
                onClick = {
                    navControllerInicio.navigate("resultadosScreen/Tênis/categoria/inicioScreen") {
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_categoria_tenis),
                    contentDescription = stringResource(id = R.string.acessar_categoria_tenis),
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.tenis),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 10.sp,
                color = Color.DarkGray,
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier.size(60.dp), contentPadding = PaddingValues(0.dp),
                onClick = {
                    navBarController.navigate("categoriasScreen") {
                        popUpTo(navControllerInicio.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // // evitar abrir novamente a mesma tela ao reselecionar mesmo item
                        launchSingleTop = true
                        // restaura o estado ao voltar para a tela anterior
                        restoreState = true
                    }
                },
                colors = ButtonDefaults.buttonColors(branco),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(id = R.string.ver_todas_categorias),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.ver_tudo),
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 10.sp,
                color = Color.DarkGray,
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Promocoes(ofertas: List<Produto>, navController: NavController) {
    val localeBR = java.util.Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 15.dp, bottom = 10.dp)
                .background(verde),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 5.dp, start = 10.dp)
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
                    text = stringResource(id = R.string.promocoes_ativas),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = stringResource(id = R.string.enquanto_durar_estoque),
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }
        }

        if (ofertas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.que_pena),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = Color.Black)
                )
                Text(
                    text = stringResource(id = R.string.nenhuma_promocao_ativa),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = Color.Black)
                )
            }
        } else {

            val cardColors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.Black,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                LazyRow {
                    items(items = ofertas) { produtoEmPromocao ->
                        //imagem do produto
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(produtoEmPromocao.imagemProduto)
                                .crossfade(true)
                                .size(coil.size.Size.ORIGINAL)
                                .build()
                        )
                        //estado para monitorar se deu erro ou nao
                        val state = painter.state

                        Card(
                            shape = RoundedCornerShape(5.dp),
                            colors = cardColors,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .clickable(enabled = true, onClick = {
                                    navController.navigate("produtoScreen/${produtoEmPromocao.idProduto}") {
                                        launchSingleTop = true
                                    }
                                }),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                Modifier
                                    .heightIn(min = 195.dp)
                                    .width(130.dp)
                                    .padding(5.dp)
                            ) {
                                when (state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .height(100.dp)
                                                .align(
                                                    Alignment.CenterHorizontally
                                                )
                                        )
                                    }

                                    is AsyncImagePainter.State.Error -> {
                                        Image(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(100.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }

                                    else -> {
                                        Image(
                                            painter = painter,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(100.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .height(35.dp)
                                        .fillMaxWidth()
                                        .padding(top = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = produtoEmPromocao.nomeProduto,
                                        fontSize = 10.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    text = numberFormat.format(produtoEmPromocao.preco.toDouble()),
                                    textDecoration = TextDecoration.LineThrough,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                val valorComDesconto =
                                    produtoEmPromocao.preco.toDouble() - ((produtoEmPromocao.preco.toDouble() * produtoEmPromocao.desconto.toDouble()))
                                Text(
                                    text = numberFormat.format(valorComDesconto),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(3.dp))
                                if (produtoEmPromocao.estoqueProduto > 0) {
                                    Text(
                                        text = "Restam ${produtoEmPromocao.estoqueProduto} unidades!",
                                        fontSize = 10.sp
                                    )
                                } else {
                                    Text(
                                        text = stringResource(id = R.string.estoque_esgotado),
                                        fontSize = 10.sp,
                                        color = Color.Red
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

@Composable
fun NavegarPorMarcas(navControllerInicio: NavHostController) {

    //valores para usar na altura, largura e padding horizontal das imagens que estao na Row
    val dpHeightItens = 70.dp
    val dpWidthItens = 130.dp
    val dpPaddingHorizontalItens = 5.dp

    Column(Modifier.padding(top = 20.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(id = R.string.navegue_por_marcas),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(color = Color.Black)
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 5.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(5.dp))
                .horizontalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_adidas),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_adidas),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Adidas/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo_fila),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_fila),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Fila/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo_mizuno),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_mizuno),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Mizuno/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo_nike),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_nike),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Nike/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo_olympikus),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_olympikus),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Olympikus/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo_puma),
                contentDescription = stringResource(R.string.acessar_categoria_produtos_puma),
                modifier = Modifier
                    .width(dpWidthItens)
                    .height(dpHeightItens)
                    .padding(horizontal = dpPaddingHorizontalItens)
                    .clickable(enabled = true, onClick = {
                        navControllerInicio.navigate("resultadosScreen/Puma/categoria/inicioScreen") {
                            launchSingleTop = true
                        }
                    })
            )
        }
    }
}
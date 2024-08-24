package com.ahpp.notshoes.util.cards

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahpp.notshoes.R
import com.ahpp.notshoes.model.ItemCarrinho
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.util.conexao.possuiConexao
import java.text.NumberFormat

@SuppressLint("DefaultLocale")
@Composable
fun CardItemCarrinho(
    item: ItemCarrinho,
    produto: Produto,
    onRemoveProduto: (ItemCarrinho) -> Unit,
    adicionarUnidade: (ItemCarrinho) -> Unit,
    removerUnidade: (ItemCarrinho) -> Unit
) {
    val ctx = LocalContext.current

    val localeBR = java.util.Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

    // imagem do produto
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(produto.imagemProduto)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build()
    )
    // estado para monitorar se deu erro ou não
    val state = painter.state

    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        produto.let { produto ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }

                        else -> {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(90.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .width(250.dp)
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            text = produto.nomeProduto,
                            fontSize = 15.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = "Cor principal: ${produto.corPrincipal}",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            text = "Tamanho: ${produto.tamanhoProduto}",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Button(
                            modifier = Modifier.size(30.dp), contentPadding = PaddingValues(0.dp),
                            onClick = {
                                if(possuiConexao(ctx)){
                                    onRemoveProduto(item)
                                } else {
                                    Toast.makeText(ctx, "Sem conexão com a internet.", Toast.LENGTH_SHORT).show()
                                }

                            },
                            colors = ButtonDefaults.buttonColors(Color.White),
                            elevation = ButtonDefaults.buttonElevation(10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Remover do carrinho.",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(
                            modifier = Modifier.size(25.dp), contentPadding = PaddingValues(0.dp),
                            onClick = {
                                if(possuiConexao(ctx)){
                                    removerUnidade(item)
                                }else{
                                    Toast.makeText(ctx, "Sem conexão com a internet.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = item.quantidade > 1,
                            colors = ButtonDefaults.buttonColors(Color.White),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = "Remover unidade.",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                            text = item.quantidade.toString(),
                            style = TextStyle(Color.Black),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Button(
                            modifier = Modifier.size(25.dp), contentPadding = PaddingValues(0.dp),
                            onClick = {
                                if(possuiConexao(ctx)){
                                    adicionarUnidade(item)
                                } else {
                                    Toast.makeText(ctx, "Sem conexão com a internet.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.White),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = "Adicionar unidade.",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = numberFormat.format(produto.preco.toDouble() * item.quantidade),
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        val percentualDesconto = 100 * produto.desconto.toDouble()
                        val percentualDescontoFormated =
                            String.format("%.0f", percentualDesconto)
                        Text(
                            text = "  (-$percentualDescontoFormated% OFF)  ",
                            color = verde,
                            fontSize = 12.sp
                        )
                        val valorComDesconto =
                            produto.preco.toDouble() - (produto.preco.toDouble() * produto.desconto.toDouble())
                        Text(
                            text = numberFormat.format(valorComDesconto * item.quantidade),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    }
                }
            }
        }
    }
}

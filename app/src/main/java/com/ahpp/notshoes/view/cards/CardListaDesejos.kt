package com.ahpp.notshoes.view.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.ahpp.notshoes.model.Produto
import java.text.NumberFormat

@Composable
fun CardListaDesejos(
    onClickProduto: () -> Unit,
    produto: Produto,
    onRemoveProduct: () -> Unit
) {
    val ctx = LocalContext.current

    val localeBR = java.util.Locale("pt", "BR")
    val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

    //imagem do produto
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(ctx)
            .data(produto.imagemProduto)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build()
    )
    //estado para monitorar se deu erro ou nao
    val state = painter.state

    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardColors(containerColor = Color.White, Color.Black, Color.Black, Color.Black),
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clickable(enabled = true, onClick = {
                onClickProduto()
            }),
        elevation = CardDefaults.cardElevation(10.dp),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
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
                        Icons.Default.Close,
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
                            .padding(start = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 5.dp, end = 5.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = produto.nomeProduto,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = numberFormat.format(produto.preco.toDouble()),
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                val valorComDesconto =
                    produto.preco.toDouble() - ((produto.preco.toDouble() * produto.desconto.toDouble()))

                Text(
                    text = numberFormat.format(valorComDesconto),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                if (produto.estoqueProduto > 0) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Em estoque. Envio imediato!",
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Estoque esgotado :(",
                        fontSize = 13.sp,
                        color = Color.Red
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.size(30.dp), contentPadding = PaddingValues(0.dp),
                    onClick = {
                        onRemoveProduct()
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Remover da lista de desejos.",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
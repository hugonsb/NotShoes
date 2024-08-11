package com.ahpp.notshoes.view.viewsLogado.viewsCarrinho

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.branco
import com.ahpp.notshoes.ui.theme.verde
import com.ahpp.notshoes.navigation.canGoBack

@Composable
fun CompraFinalizadaScreen(navControllerCarrinho: NavController) {

    BackHandler {
        navControllerCarrinho.popBackStack("carrinhoScreen", false)
    }

    val color = remember { Animatable(verde) }
    LaunchedEffect(Unit) {
        color.animateTo(azulEscuro, animationSpec = tween(1500))
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
                .background(color.value)
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
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = stringResource(id = R.string.toque_para_voltar_description),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.padding(top = 70.dp))
            Image(
                painter = painterResource(id = R.drawable.baseline_favorite_filled_24),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(R.string.obrigado_por_comprar_conosco), fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    Color.White
                )
            )
            Spacer(Modifier.padding(top = 20.dp))
            Text(
                modifier = Modifier.padding(horizontal = 30.dp),
                text = stringResource(R.string.voc_pode_acompanhar_seus_pedidos_em_perfil_pedidos),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    Color.White
                )
            )
        }
    }
}
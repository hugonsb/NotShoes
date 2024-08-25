package com.ahpp.notshoes.view.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahpp.notshoes.model.Venda
import java.time.format.DateTimeFormatter

@Composable
fun CardPedidos(venda: Venda) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            Color.Black,
            Color.Black,
            Color.Black
        ),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text("Data: ${venda.dataPedido.format(outputFormatter)}")
            Spacer(Modifier.height(5.dp))
            Text("Status: ${venda.status}")
            Spacer(Modifier.height(10.dp))
            Text(venda.detalhesPedido)
        }
    }
}
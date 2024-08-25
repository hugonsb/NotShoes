package com.ahpp.notshoes.view.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.model.Endereco

@Composable
fun CardEnderecoCarrinho(enderecoParaEntrega: Endereco){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if (enderecoParaEntrega.idEndereco == clienteLogado.idEnderecoPrincipal) {
            Text(
                modifier = Modifier.padding(bottom = 3.dp),
                text = "Endereço principal",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text("${enderecoParaEntrega.endereco} - ${enderecoParaEntrega.complemento}")
        Text("Número: ${enderecoParaEntrega.numero}")
        Text("CEP: ${enderecoParaEntrega.cep}")
        Text("${enderecoParaEntrega.cidade} - ${enderecoParaEntrega.estado}")
        Text(enderecoParaEntrega.bairro)
    }
}
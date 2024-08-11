package com.ahpp.notshoes.constantes

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ahpp.notshoes.model.Cliente
import com.ahpp.notshoes.ui.theme.azulEscuro

//variavel que mantem o objeto cliente logado
lateinit var clienteLogado: Cliente

//key para acessar o id do usuario no datastore
val usuarioLogadoPreferences = stringPreferencesKey("user_id")

//cores para os campos de texto
object CustomTextFieldColors {
    @Composable
    fun colorsTextFields() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFEEF3F5),
        focusedContainerColor = Color(0xFFEEF3F5),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        unfocusedBorderColor = Color(0xFFEEF3F5),
        focusedBorderColor = azulEscuro,
        focusedLabelColor = Color.Black,
        errorContainerColor = Color(0xFFEEF3F5),
        cursorColor = azulEscuro,
    )
}

object ColorsExposedDropdownMenu {
    @Composable
    fun customColorsExposedDropdownMenu() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFEEF3F5),
        focusedContainerColor = Color(0xFFEEF3F5),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        unfocusedBorderColor = Color(0xFFEEF3F5),
        focusedBorderColor = Color(0xFFEEF3F5),
        focusedLabelColor = Color(0xFF000000),
        cursorColor = azulEscuro
    )
}

object FiltrosList{
    val coresList = listOf(
        "Cor",
        "Amarelo",
        "Anil",
        "Azul",
        "Azul Claro",
        "Azul Escuro",
        "Bege",
        "Branco",
        "Bronze",
        "Cinza",
        "Ciano",
        "Dourado",
        "Laranja",
        "Laranja Claro",
        "Laranja Escuro",
        "Magenta",
        "Marrom",
        "Preto",
        "Prata",
        "Rosa",
        "Roxo",
        "Turquesa",
        "Verde",
        "Verde Claro",
        "Verde Limão",
        "Verde Oliva",
        "Vermelho",
        "Vermelho Escuro",
        "Violeta"
    )
    val precosList = listOf(
        "Preço",
        "Menos de R$ 100",
        "R$ 100 até R$ 299",
        "R$ 300 até R$ 499",
        "R$ 500 até R$ 699",
        "R$ 700 até R$ 899",
        "R$ 900 até R$ 1000",
        "Acima de R$ 1000"
    )
    val tamanhosList = listOf(
        "Tamanho",
        "P",
        "M",
        "G",
        "GG",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48"
    )
    val tipoOrdenacaoList = listOf(
        "Ordenar por",
        "Menor preço",
        "Maior preço",
        "Ofertas",
    )
}
package com.ahpp.notshoes.states.screensReutilizaveis

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ahpp.notshoes.constantes.FiltrosList.coresList
import com.ahpp.notshoes.constantes.FiltrosList.precosList
import com.ahpp.notshoes.constantes.FiltrosList.tamanhosList
import com.ahpp.notshoes.constantes.FiltrosList.tipoOrdenacaoList
import com.ahpp.notshoes.model.Produto

data class ResultadosScreenState(
    val produtosList: List<Produto> = emptyList(),

    val expandedCoresList: Boolean = false,
    val cor: String = coresList[0],

    val expandedPrecosList: Boolean = false,
    val preco: String = precosList[0],

    val expandedTamanhosList: Boolean = false,
    val tamanho: String = tamanhosList[0],

    val expandedtipoOrdenacaoList: Boolean = false,
    val tipoOrdenacao: String = tipoOrdenacaoList[0],

    val expandedFiltro: Boolean = false,

    val isLoading: Boolean = true,

    val favoritos: SnapshotStateMap<Int, String> = mutableStateMapOf(),

    val valorBusca: String = "",
    val tipoBusca: String = ""
)
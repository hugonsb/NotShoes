package com.ahpp.notshoes.states.logado.perfil

import com.ahpp.notshoes.model.Venda

data class PedidosScreenState(
    val pedidosList : List<Venda> = emptyList(),
    val isLoading : Boolean = true
)
package com.ahpp.notshoes.states.logado.listaDesejos

import com.ahpp.notshoes.model.Produto

data class ListaDesejosScreenState (
    val produtosList: List<Produto> = emptyList(),
    val isLoading: Boolean = true
)
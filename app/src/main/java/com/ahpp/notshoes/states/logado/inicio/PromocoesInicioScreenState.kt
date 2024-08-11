package com.ahpp.notshoes.states.logado.inicio

import com.ahpp.notshoes.model.Produto

data class PromocoesInicioScreenState (
    val ofertas: List<Produto> = emptyList()
)
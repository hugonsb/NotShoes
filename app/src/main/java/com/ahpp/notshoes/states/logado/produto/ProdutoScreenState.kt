package com.ahpp.notshoes.states.logado.produto

import com.ahpp.notshoes.model.Produto

data class ProdutoScreenState(
    val produto: Produto? = null,
    val favorito: Boolean = false,
    val isLoading: Boolean = true
)
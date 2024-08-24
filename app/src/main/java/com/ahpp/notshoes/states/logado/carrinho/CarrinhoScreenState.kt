package com.ahpp.notshoes.states.logado.carrinho

import com.ahpp.notshoes.model.Endereco
import com.ahpp.notshoes.model.ItemCarrinho
import com.ahpp.notshoes.model.Produto

data class CarrinhoScreenState (
    val itensList: List<ItemCarrinho> = emptyList(),
    val valorTotal: Double = 0.0,
    val valorTotalComDesconto: Double = 0.0,
    val detalhesPedido: String = "",
    val combinedList: List<Pair<ItemCarrinho, Produto>> = emptyList(),
    val enderecosList: List<Endereco> = emptyList(),
    val enderecoParaEntrega: Endereco? = null,
    val isLoading: Boolean = true
)
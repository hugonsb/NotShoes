package com.ahpp.notshoes.util.filtros

import com.ahpp.notshoes.model.Produto

fun filtrarProdutosPorCor(produtosList: List<Produto>, cor: String): List<Produto> {
    return produtosList.filter { it.corPrincipal.equals(cor, ignoreCase = true) }
}

fun filtrarProdutosPorTamanho(produtosList: List<Produto>, tamanho: String): List<Produto> {
    return produtosList.filter { it.tamanhoProduto.equals(tamanho, ignoreCase = false) }
}

fun filtrarProdutosPeloPreco(produtosList: List<Produto>, preco: String): List<Produto> {
    return produtosList.filter { produto ->

        val valorComDesconto =
            produto.preco.toDouble() - ((produto.preco.toDouble() * produto.desconto.toDouble()))

        when (preco) {
            "Menos de R$ 100" -> valorComDesconto < 100.0
            "R$ 100 até R$ 299" -> valorComDesconto in 100.0..299.0
            "R$ 300 até R$ 499" -> valorComDesconto in 300.0..499.0
            "R$ 500 até R$ 699" -> valorComDesconto in 500.0..699.0
            "R$ 700 até R$ 899" -> valorComDesconto in 700.0..899.0
            "R$ 900 até R$ 1000" -> valorComDesconto in 900.0..1000.0
            "Acima de R$ 1000" -> valorComDesconto > 1000.0
            else -> true
        }
    }
}

fun calcularValorComDesconto(preco: String, desconto: String): Double {
    val valorComDesconto = preco.toDouble() - (preco.toDouble() * desconto.toDouble())
    return valorComDesconto
}
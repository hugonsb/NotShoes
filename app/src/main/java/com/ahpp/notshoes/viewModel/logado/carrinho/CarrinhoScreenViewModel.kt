package com.ahpp.notshoes.viewModel.logado.carrinho

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.carrinho.CarrinhoRepository
import com.ahpp.notshoes.data.carrinho.getItensCarrinho
import com.ahpp.notshoes.data.carrinho.getProdutoCarrinho
import com.ahpp.notshoes.data.endereco.getEnderecos
import com.ahpp.notshoes.model.Endereco
import com.ahpp.notshoes.model.ItemCarrinho
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.states.logado.carrinho.CarrinhoScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.NumberFormat

class CarrinhoScreenViewModel : ViewModel() {

    private val _carrinhoScreenState = MutableStateFlow(CarrinhoScreenState())
    val carrinhoScreenState: StateFlow<CarrinhoScreenState> = _carrinhoScreenState.asStateFlow()

    init {
        atualizarCarrinho()
    }

    fun atualizarCarrinho() {
        val localeBR = java.util.Locale("pt", "BR")
        val numberFormat = NumberFormat.getCurrencyInstance(localeBR)

        viewModelScope.launch {
            atualizarEnderecosCarrinho()
            _carrinhoScreenState.value = _carrinhoScreenState.value.copy(
                isLoading = true
            )

            val itens = getItensCarrinho(clienteLogado.idCarrinho)
            val produtos = getProdutoCarrinho(clienteLogado.idCarrinho)

            // Combine itensList e produtosList
            val combined = itens.mapNotNull { item ->
                val produto = produtos.find { it.idProduto == item.idProduto }
                if (produto != null) {
                    item to produto
                } else {
                    null
                }
            }

            // itensList, detalhesPedido e valorComDesconto serão usados em finalidarPedidoScreen
            _carrinhoScreenState.value = _carrinhoScreenState.value.copy(
                itensList = itens,
                combinedList = combined,
                valorTotal = calcularValorCarrinhoTotal(itens, produtos),
                valorTotalComDesconto = calcularValorCarrinhoComDesconto(itens, produtos),
                detalhesPedido = combined.joinToString(separator = "\n\n") { item ->
                    val valorComDesconto =
                        item.second.preco.toDouble() - (item.second.preco.toDouble() * item.second.desconto.toDouble())
                    "(${item.first.quantidade}) ${item.second.nomeProduto} - R$ ${
                        numberFormat.format(
                            valorComDesconto * item.first.quantidade
                        )
                    }"
                },
            )
            _carrinhoScreenState.value = _carrinhoScreenState.value.copy(
                isLoading = false
            )
        }
    }

    fun adicionarUnidade(ctx: Context, item: ItemCarrinho, onSuccess: () -> Unit) {
        val carrinhoRepository = CarrinhoRepository(
            item.idProduto,
            clienteLogado.idCliente
        )

        carrinhoRepository.adicionarItemCarrinho(object :
            CarrinhoRepository.Callback {
            override fun onSuccess(codigoRecebido: String) {
                // -1 estoque insuficiente, 0 erro, 1 sucesso
                when (codigoRecebido) {
                    "-1" -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            ctx,
                            "Estoque insuficiente.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    "0" -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                            .show()
                    }

                    "1" -> onSuccess()

                    else -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            ctx,
                            "Erro de rede.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(e: IOException) {
                // erro de rede
                // não é possível mostrar um Toast de um Thread
                // que não seja UI, então é feito dessa forma
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("Erro: ", e.message.toString())
            }
        })
    }

    fun removerUnidade(ctx: Context, item: ItemCarrinho, onSuccess: () -> Unit) {
        val carrinhoRepository = CarrinhoRepository(
            item.idProduto,
            clienteLogado.idCliente
        )

        carrinhoRepository.removerItemCarrinho(object :
            CarrinhoRepository.Callback {
            override fun onSuccess(codigoRecebido: String) {
                // -1 estoque insuficiente, 0 erro, 1 sucesso
                when (codigoRecebido) {
                    "0" -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                            .show()
                    }

                    "1" -> onSuccess()

                    else -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            ctx,
                            "Erro de rede.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(e: IOException) {
                // erro de rede
                // não é possível mostrar um Toast de um Thread
                // que não seja UI, então é feito dessa forma
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("Erro: ", e.message.toString())
            }
        })
    }

    fun removerProduto(ctx: Context, item: ItemCarrinho, onSuccess: () -> Unit) {
        val carrinhoRepository = CarrinhoRepository(
            item.idProduto,
            clienteLogado.idCliente
        )

        carrinhoRepository.removerProdutoCarrinho(object :
            CarrinhoRepository.Callback {
            override fun onSuccess(codigoRecebido: String) {
                // -1 erro, 1 sucesso
                Log.i(
                    "CODIGO RECEBIDO (REMOVER ITEM DO CARRINHO): ",
                    codigoRecebido
                )
                when (codigoRecebido) {

                    "1" -> onSuccess()

                    else -> Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            ctx,
                            "Erro de rede.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(e: IOException) {
                // erro de rede
                // não é possível mostrar um Toast de um Thread
                // que não seja UI, então é feito dessa forma
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("Erro (remover produto carrinho): ", e.message.toString())
            }
        })
    }

    private fun calcularValorCarrinhoTotal(
        itensList: List<ItemCarrinho>,
        produtosList: List<Produto>
    ): Double {
        return itensList.sumOf { item ->
            val produto = produtosList.find { it.idProduto == item.idProduto }
            produto?.let {
                produto.preco.toDouble() * item.quantidade
            } ?: 0.0
        }
    }

    private fun calcularValorCarrinhoComDesconto(
        itensList: List<ItemCarrinho>,
        produtosList: List<Produto>
    ): Double {
        return itensList.sumOf { item ->
            val produto = produtosList.find { it.idProduto == item.idProduto }
            produto?.let {
                val precoFinal = produto.preco.toDouble() * (1.0 - produto.desconto.toDouble())
                precoFinal * item.quantidade
            } ?: 0.0
        }
    }

    private fun atualizarEnderecosCarrinho() {
        viewModelScope.launch {
            val enderecosList = getEnderecos(clienteLogado.idCliente)

            val enderecoParaEntrega = if (clienteLogado.idEnderecoPrincipal != -1) {
                enderecosList.find { it.idEndereco == clienteLogado.idEnderecoPrincipal }
            } else {
                enderecosList.firstOrNull()
            }

            val outrosEnderecos = if (enderecoParaEntrega != null) {
                enderecosList.filter { it.idEndereco != enderecoParaEntrega.idEndereco }
            } else {
                emptyList()
            }

            val listaEnderecosOrganizada = listOfNotNull(enderecoParaEntrega) + outrosEnderecos

            _carrinhoScreenState.value = _carrinhoScreenState.value.copy(
                enderecosList = listaEnderecosOrganizada,
                enderecoParaEntrega = enderecoParaEntrega,
            )
        }
    }

    fun setEnderecoParaEntrega(endereco: Endereco) {
        _carrinhoScreenState.value = _carrinhoScreenState.value.copy(
            enderecoParaEntrega = endereco
        )
    }
}
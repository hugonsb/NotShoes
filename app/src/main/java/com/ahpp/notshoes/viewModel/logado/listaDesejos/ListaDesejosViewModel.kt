package com.ahpp.notshoes.viewModel.logado.listaDesejos

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.produto.ProdutoRepository
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.states.logado.listaDesejos.ListaDesejosScreenState
import com.ahpp.notshoes.util.conexao.possuiConexao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaDesejosViewModel(
    private val produtoRepository: ProdutoRepository
) : ViewModel() {

    private val _listaDesejosState = MutableStateFlow(ListaDesejosScreenState())
    val listaDesejosState: StateFlow<ListaDesejosScreenState> = _listaDesejosState.asStateFlow()

    init {
        atualizarListaDesejos()
    }

    fun atualizarListaDesejos() {
        viewModelScope.launch {
            _listaDesejosState.value = _listaDesejosState.value.copy(
                isLoading = true
            )
            _listaDesejosState.update { currentState ->
                currentState.copy(
                    produtosList = produtoRepository.getProdutosListaDesejos(clienteLogado.idListaDesejos),
                    isLoading = false
                )
            }
        }
    }

    fun removerProdutoListaDesejos(produtoId: Int, ctx: Context) {
        if (possuiConexao(ctx)) {
            viewModelScope.launch {

                _listaDesejosState.update { currentState ->
                    currentState.copy(
                        isLoading = true
                    )
                }

                produtoRepository.removerProdutoListaDesejos(produtoId, clienteLogado.idCliente)
                // Adicionando um pequeno atraso para garantir que a remoção seja processada
                kotlinx.coroutines.delay(500)
                atualizarListaDesejos()
            }
        } else {
            Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT).show()
        }
    }
}

fun adicionarListaDesejos(adicionadoListaDesejosCheck: String, produto: Produto): String {
    val produtoRepository = ProdutoRepository()
    if (adicionadoListaDesejosCheck == "0") {
        produtoRepository.adicionarProdutoListaDesejos(
            produto.idProduto, clienteLogado.idCliente
        )
        return "1"
    } else if (adicionadoListaDesejosCheck == "1") {
        produtoRepository.removerProdutoListaDesejos(
            produto.idProduto,
            clienteLogado.idCliente
        )
        return "0"
    }
    return "0"
}
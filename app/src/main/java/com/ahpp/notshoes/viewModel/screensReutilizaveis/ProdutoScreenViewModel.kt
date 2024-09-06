package com.ahpp.notshoes.viewModel.screensReutilizaveis

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.produto.ProdutoRepository
import com.ahpp.notshoes.data.produto.adicionarProdutoCarrinho
import com.ahpp.notshoes.states.logado.produto.ProdutoScreenState
import com.ahpp.notshoes.util.conexao.possuiConexao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProdutoScreenViewModel(
    private val produtoRepository: ProdutoRepository,
    produtoId: Int
) : ViewModel() {

    private val _produtoScreenState = MutableStateFlow(ProdutoScreenState())
    val produtoScreenState: StateFlow<ProdutoScreenState> = _produtoScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            viewModelScope.launch {
                _produtoScreenState.value = _produtoScreenState.value.copy(
                    isLoading = true
                )

                var favorito = false
                produtoRepository.verificarProdutoListaDesejos(
                    produtoId,
                    clienteLogado.idListaDesejos
                ) {
                    favorito = it == "1"
                }

                _produtoScreenState.update { currentState ->
                    currentState.copy(
                        produto = produtoRepository.getProdutoId(produtoId.toString()),
                        isLoading = false,
                        favorito = favorito
                    )
                }
            }
        }
    }

    fun adicionarListaDesejos(produtoId: Int, ctx: Context) {
        if (possuiConexao(ctx)) {
            if (!_produtoScreenState.value.favorito) {
                produtoRepository.adicionarProdutoListaDesejos(
                    produtoId, clienteLogado.idCliente
                )
                _produtoScreenState.update { currentState ->
                    currentState.copy(
                        favorito = true
                    )
                }
            } else {
                produtoRepository.removerProdutoListaDesejos(
                    produtoId, clienteLogado.idCliente
                )
                _produtoScreenState.update { currentState ->
                    currentState.copy(
                        favorito = false
                    )
                }
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ctx,
                    R.string.verifique_conexao_internet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun adicionarProdutoCarrinho(produtoId: Int, ctx: Context) {
        if (possuiConexao(ctx)) {
            adicionarProdutoCarrinho(ctx, produtoId)
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    ctx,
                    R.string.verifique_conexao_internet,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
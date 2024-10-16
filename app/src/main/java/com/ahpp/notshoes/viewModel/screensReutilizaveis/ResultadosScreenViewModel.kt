package com.ahpp.notshoes.viewModel.screensReutilizaveis

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.FiltrosList.coresList
import com.ahpp.notshoes.constantes.FiltrosList.precosList
import com.ahpp.notshoes.constantes.FiltrosList.tamanhosList
import com.ahpp.notshoes.constantes.FiltrosList.tipoOrdenacaoList
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.produto.ProdutoRepository
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.states.screensReutilizaveis.ResultadosScreenState
import com.ahpp.notshoes.util.conexao.possuiConexao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultadosScreenViewModel(
    private val produtoRepository: ProdutoRepository,
    ctx: Context,
    tipoBusca: String,
    valorBusca: String
) : ViewModel() {
    private val _resultadosScreenState = MutableStateFlow(
        ResultadosScreenState(
            valorBusca = valorBusca,
            tipoBusca = tipoBusca
        )
    )
    val resultadosScreenState: StateFlow<ResultadosScreenState> =
        _resultadosScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            buscarProduto(ctx, valorBusca, tipoBusca)
        }
    }

    fun buscarProduto(ctx: Context, valorBusca: String, tipoBusca: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _resultadosScreenState.value = _resultadosScreenState.value.copy(
                isLoading = true
            )
            if (!possuiConexao(ctx)) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(ctx, R.string.verifique_conexao_internet, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                var produtosList: List<Produto> = when (tipoBusca) {
                    "nome" -> {
                        produtoRepository.buscarProdutoNome(valorBusca)
                    }

                    "categoria" -> {
                        produtoRepository.filtrarProdutoCategoria(valorBusca)
                    }

                    else -> {
                        //buscar produtos por intervalo de preço
                        produtoRepository.getProdutosFiltroIntervalo(valorBusca)
                    }
                }

                produtosList =
                    filtrarProdutos(
                        produtosList,
                        _resultadosScreenState.value.cor,
                        _resultadosScreenState.value.tamanho,
                        _resultadosScreenState.value.preco,
                        _resultadosScreenState.value.tipoOrdenacao
                    )

                // verifica quais produtos estao favoritados 1 = sim 0 = não
                val favoritos = mutableStateMapOf<Int, String>()
                produtosList.forEach { produto ->
                    produtoRepository.verificarProdutoListaDesejos(
                        produto.idProduto,
                        clienteLogado.idListaDesejos
                    ) {
                        favoritos[produto.idProduto] = it
                    }
                }

                _resultadosScreenState.update { currentState ->
                    currentState.copy(
                        produtosList = produtosList,
                        favoritos = favoritos,
                    )
                }
            }
            _resultadosScreenState.update { currentState ->
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }

    fun setCor(cor: String) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                cor = cor,
            )
        }
    }

    fun setTamanho(tamanho: String) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                tamanho = tamanho,
            )
        }
    }

    fun setPreco(preco: String) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                preco = preco,
            )
        }
    }

    fun setTipoOrdenacao(tipoOrdenacao: String) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                tipoOrdenacao = tipoOrdenacao,
            )
        }
    }

    fun setExpandedFiltro() {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                expandedFiltro = !_resultadosScreenState.value.expandedFiltro,
            )
        }
    }

    fun setExpandedCoresList(expanded: Boolean) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                expandedCoresList = expanded
            )
        }
    }

    fun setExpandedPrecosList(expanded: Boolean) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                expandedPrecosList = expanded
            )
        }
    }

    fun setExpandedTamanhosList(expanded: Boolean) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                expandedTamanhosList = expanded,
            )
        }
    }

    fun setExpandedtipoOrdenacaoList(expanded: Boolean) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                expandedtipoOrdenacaoList = expanded,
            )
        }
    }

    fun resetFiltros(ctx: Context) {
        _resultadosScreenState.update { currentState ->
            currentState.copy(
                cor = coresList[0],
                tamanho = tamanhosList[0],
                preco = precosList[0],
                tipoOrdenacao = tipoOrdenacaoList[0]
            )
        }
        buscarProduto(ctx, _resultadosScreenState.value.valorBusca, _resultadosScreenState.value.tipoBusca)
    }

    fun adicionarListaDesejos(adicionadoListaDesejosCheck: String, produto: Produto): String {
        if (adicionadoListaDesejosCheck == "0") {
            produtoRepository.adicionarProdutoListaDesejos(
                produto.idProduto, clienteLogado.idCliente
            )
            // Atualizar o mapa de favoritos
            val novosFavoritos = _resultadosScreenState.value.favoritos
            novosFavoritos[produto.idProduto] = "1"  // "1" para indicar que foi adicionado
            _resultadosScreenState.update { currentState ->
                currentState.copy(
                    favoritos = novosFavoritos
                )
            }
            return "1"
        } else if (adicionadoListaDesejosCheck == "1") {
            produtoRepository.removerProdutoListaDesejos(
                produto.idProduto,
                clienteLogado.idCliente
            )
            // Atualizar o mapa de favoritos
            val novosFavoritos = _resultadosScreenState.value.favoritos
            novosFavoritos.remove(produto.idProduto)  // Remover do mapa
            _resultadosScreenState.update { currentState ->
                currentState.copy(
                    favoritos = novosFavoritos
                )
            }
            return "0"
        }
        return "0"
    }

    private fun ordenarProdutos(
        produtosList: List<Produto>,
        tipoOrdenacao: String
    ): List<Produto> {
        return when (tipoOrdenacao) {
            "Menor preço" -> produtosList.sortedBy {
                calcularValorComDesconto(
                    it.preco,
                    it.desconto
                )
            }

            "Maior preço" -> produtosList.sortedByDescending {
                calcularValorComDesconto(
                    it.preco,
                    it.desconto
                )
            }

            "Ofertas" -> {
                val (ofertas, naoOfertas) = produtosList.partition { it.emOferta }
                ofertas + naoOfertas
            }

            else -> produtosList
        }
    }

    private fun filtrarProdutos(
        produtosList: List<Produto>,
        cor: String,
        tamanho: String,
        preco: String,
        tipoOrdenacao: String
    ): List<Produto> {
        var produtosFiltrados = produtosList

        if (cor != "Cor") {
            produtosFiltrados = filtrarProdutosPorCor(produtosFiltrados, cor)
        }

        if (tamanho != "Tamanho") {
            produtosFiltrados = filtrarProdutosPorTamanho(produtosFiltrados, tamanho)
        }

        if (preco != "Preço") {
            produtosFiltrados = filtrarProdutosPeloPreco(produtosFiltrados, preco)
        }

        produtosFiltrados = ordenarProdutos(produtosFiltrados, tipoOrdenacao)

        return produtosFiltrados
    }

    private fun filtrarProdutosPorCor(produtosList: List<Produto>, cor: String): List<Produto> {
        return produtosList.filter { it.corPrincipal.equals(cor, ignoreCase = true) }
    }

    private fun filtrarProdutosPorTamanho(produtosList: List<Produto>, tamanho: String): List<Produto> {
        return produtosList.filter { it.tamanhoProduto.equals(tamanho, ignoreCase = false) }
    }

    private fun filtrarProdutosPeloPreco(produtosList: List<Produto>, preco: String): List<Produto> {
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

    private fun calcularValorComDesconto(preco: String, desconto: String): Double {
        val valorComDesconto = preco.toDouble() - (preco.toDouble() * desconto.toDouble())
        return valorComDesconto
    }
}
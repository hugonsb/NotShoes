package com.ahpp.notshoes.viewModel.logado.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.data.produto.ProdutoRepository
import com.ahpp.notshoes.states.logado.inicio.PromocoesInicioScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PromocoesInicioScreenViewModel(
    private val produtoRepository: ProdutoRepository
) : ViewModel() {

    private val _promocoesInicioScreenState = MutableStateFlow(PromocoesInicioScreenState())
    val promocoesInicioScreenState: StateFlow<PromocoesInicioScreenState> =
        _promocoesInicioScreenState.asStateFlow()

    init {
        getPromocoes()
    }

    fun getPromocoes() {
        viewModelScope.launch {
            _promocoesInicioScreenState.update { currentState ->
                currentState.copy(
                    ofertas = produtoRepository.getPromocoes()
                )
            }
        }
    }
}
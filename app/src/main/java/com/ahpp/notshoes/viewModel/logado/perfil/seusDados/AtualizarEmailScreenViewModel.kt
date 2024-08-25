package com.ahpp.notshoes.viewModel.logado.perfil.seusDados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.states.logado.perfil.seusDados.AtualizarEmailScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AtualizarEmailScreenViewModel : ViewModel() {

    private val _atualizarEmailScreenState = MutableStateFlow(AtualizarEmailScreenState())
    val atualizarEmailScreenState: StateFlow<AtualizarEmailScreenState> =
        _atualizarEmailScreenState.asStateFlow()

    fun setNovoEmail(novoEmail: String) {
        _atualizarEmailScreenState.update { currentState ->
            currentState.copy(
                emailNovo = novoEmail
            )
        }
    }

    fun atualizarClienteLogado() {
        viewModelScope.launch {
            clienteLogado = getCliente(clienteLogado.idCliente)
        }
    }
}
package com.ahpp.notshoes.viewModel.logado.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getPedidos
import com.ahpp.notshoes.states.logado.perfil.PedidosScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PedidosScreenViewModel : ViewModel() {
    private val _pedidosScreenState = MutableStateFlow(PedidosScreenState())
    val pedidosScreenState: StateFlow<PedidosScreenState> = _pedidosScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            _pedidosScreenState.update { currentState ->
                currentState.copy(
                    pedidosList = getPedidos(clienteLogado.idCliente),
                    isLoading = false
                )
            }
        }
    }
}
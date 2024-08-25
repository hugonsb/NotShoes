package com.ahpp.notshoes.viewModel.logado.perfil.enderecos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.endereco.getEnderecos
import com.ahpp.notshoes.states.logado.perfil.enderecos.EnderecosScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnderecosScreenViewModel : ViewModel() {

    private val _enderecosScreenState = MutableStateFlow(EnderecosScreenState())
    val enderecosScreenState: StateFlow<EnderecosScreenState> = _enderecosScreenState.asStateFlow()

    init {
        atualizarEnderecos()
    }

    fun atualizarEnderecos() {
        viewModelScope.launch {
            _enderecosScreenState.value = _enderecosScreenState.value.copy(
                isLoading = true
            )
            _enderecosScreenState.update { currentState ->
                currentState.copy(
                    enderecosList = getEnderecos(clienteLogado.idCliente),
                    isLoading = false
                )
            }
        }
    }
}
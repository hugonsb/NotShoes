package com.ahpp.notshoes.viewModel.logado.perfil.enderecos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.states.logado.perfil.enderecos.CadastrarEnderecoScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CadastrarEnderecoScreenViewModel : ViewModel() {
    private val _cadastrarEnderecoScreenState = MutableStateFlow(CadastrarEnderecoScreenState())
    val cadastrarEnderecoScreenState: StateFlow<CadastrarEnderecoScreenState> =
        _cadastrarEnderecoScreenState.asStateFlow()

    fun setCep(cep: String) {
        _cadastrarEnderecoScreenState.value = _cadastrarEnderecoScreenState.value.copy(cep = cep)
    }

    fun setEndereco(endereco: String) {
        _cadastrarEnderecoScreenState.value =
            _cadastrarEnderecoScreenState.value.copy(endereco = endereco)
    }

    fun setNumero(numero: String) {
        _cadastrarEnderecoScreenState.value =
            _cadastrarEnderecoScreenState.value.copy(numero = numero)
    }

    fun setBairro(bairro: String) {
        _cadastrarEnderecoScreenState.value =
            _cadastrarEnderecoScreenState.value.copy(bairro = bairro)
    }

    fun setCidade(cidade: String) {
        _cadastrarEnderecoScreenState.value =
            _cadastrarEnderecoScreenState.value.copy(cidade = cidade)
    }

    fun setComplemento(complemento: String) {
        _cadastrarEnderecoScreenState.value =
            _cadastrarEnderecoScreenState.value.copy(complemento = complemento)
    }

    fun atualizarClienteLogado() {
        viewModelScope.launch {
            clienteLogado = getCliente(clienteLogado.idCliente)
        }
    }
}
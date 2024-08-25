package com.ahpp.notshoes.viewModel.logado.perfil.enderecos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.states.logado.perfil.enderecos.EditarEnderecoScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditarEnderecoScreenViewModel : ViewModel() {
    private val _editarEnderecoScreenState = MutableStateFlow(EditarEnderecoScreenState())
    val editarEnderecoScreenState: StateFlow<EditarEnderecoScreenState> =
        _editarEnderecoScreenState.asStateFlow()

    fun setCep(cep: String) {
        _editarEnderecoScreenState.value = _editarEnderecoScreenState.value.copy(cep = cep)
    }

    fun setEndereco(endereco: String) {
        _editarEnderecoScreenState.value =
            _editarEnderecoScreenState.value.copy(endereco = endereco)
    }

    fun setNumero(numero: String) {
        _editarEnderecoScreenState.value = _editarEnderecoScreenState.value.copy(numero = numero)
    }

    fun setBairro(bairro: String) {
        _editarEnderecoScreenState.value = _editarEnderecoScreenState.value.copy(bairro = bairro)
    }

    fun setCidade(cidade: String) {
        _editarEnderecoScreenState.value = _editarEnderecoScreenState.value.copy(cidade = cidade)
    }

    fun setComplemento(complemento: String) {
        _editarEnderecoScreenState.value =
            _editarEnderecoScreenState.value.copy(complemento = complemento)
    }

    fun atualizarClienteLogado() {
        viewModelScope.launch {
            clienteLogado = getCliente(clienteLogado.idCliente)
        }
    }

}
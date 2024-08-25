package com.ahpp.notshoes.viewModel.logado.perfil.seusDados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.states.logado.perfil.seusDados.AtualizarDadosPessoaisScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AtualizarDadosPessoaisScreenViewModel : ViewModel() {

    private val _atualizarDadosPessoaisScreenState =
        MutableStateFlow(AtualizarDadosPessoaisScreenState())
    val atualizarDadosPessoaisScreenState: StateFlow<AtualizarDadosPessoaisScreenState> =
        _atualizarDadosPessoaisScreenState.asStateFlow()

    fun setNome(nome: String) {
        _atualizarDadosPessoaisScreenState.update { currentState ->
            currentState.copy(
                nomeNovo = nome
            )
        }
    }

    fun setCpf(cpf: String) {
        _atualizarDadosPessoaisScreenState.update { currentState ->
            currentState.copy(
                cpfNovo = cpf
            )
        }
    }

    fun setTelefone(telefone: String) {
        _atualizarDadosPessoaisScreenState.update { currentState ->
            currentState.copy(
                telefoneContatoNovo = telefone
            )
        }
    }

    fun setGenero(genero: String) {
        _atualizarDadosPessoaisScreenState.update { currentState ->
            currentState.copy(
                generoNovo = genero
            )
        }
    }

    fun atualizarClienteLogado() {
        viewModelScope.launch {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }
}
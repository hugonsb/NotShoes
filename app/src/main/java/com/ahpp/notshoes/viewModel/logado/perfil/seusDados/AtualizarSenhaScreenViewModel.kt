package com.ahpp.notshoes.viewModel.logado.perfil.seusDados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.states.logado.perfil.seusDados.AtualizarSenhaScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest

class AtualizarSenhaScreenViewModel : ViewModel() {
    private val _atualizarSenhaScreenState = MutableStateFlow(AtualizarSenhaScreenState())
    val atualizarSenhaScreenState: StateFlow<AtualizarSenhaScreenState> =
        _atualizarSenhaScreenState.asStateFlow()

    fun setSenhaAtual(senhaAtual: String) {
        _atualizarSenhaScreenState.update { currentState ->
            currentState.copy(
                senhaAtual = senhaAtual
            )
        }
    }

    fun setNovaSenha(novaSenha: String) {
        _atualizarSenhaScreenState.update { currentState ->
            currentState.copy(
                senhaNova = novaSenha
            )
        }
    }

    fun atualizarClienteLogado() {
        viewModelScope.launch {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }

    fun md5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun senhaAtualCorreta(senhaAtualCriptografada: String): Boolean {
        return senhaAtualCriptografada == clienteLogado.senha
    }
}
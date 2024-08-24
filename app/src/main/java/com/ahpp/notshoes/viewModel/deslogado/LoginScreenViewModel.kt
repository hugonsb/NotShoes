package com.ahpp.notshoes.viewModel.deslogado

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.constantes.usuarioLogadoPreferences
import com.ahpp.notshoes.data.cliente.LoginCliente
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.dataStore
import com.ahpp.notshoes.states.deslogado.LoginScreenState
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.util.validacao.ValidarCamposDados.validarEmail
import com.ahpp.notshoes.util.validacao.ValidarCamposDados.validarSenha
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class LoginScreenViewModel(ctx: Context) : ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()

    private val _idUsuario = MutableStateFlow("-2")
    val idUsuario: StateFlow<String> = _idUsuario.asStateFlow()

    init {
        viewModelScope.launch {
            // Inicialize o idUsuario verificando se já há um usuário logado
            ctx.dataStore.data.collect { preferences ->
                _idUsuario.value = preferences[usuarioLogadoPreferences] ?: "-1"
            }
        }
    }

    fun setEmail(email: String) {
        _loginScreenState.update { currentState ->
            currentState.copy(
                email = email,
                emailValido = validarEmail(email),
                dadosIncorretos = false
            )
        }
    }

    fun setSenha(senha: String) {
        _loginScreenState.update { currentState ->
            currentState.copy(
                senha = senha,
                senhaValida = validarSenha(senha),
                dadosIncorretos = false
            )
        }
    }

    fun sendLogin(ctx: Context) {

        val email = _loginScreenState.value.email
        val senha = _loginScreenState.value.senha

        _loginScreenState.update { currentState ->
            currentState.copy(
                emailValido = validarEmail(email),
                senhaValida = validarSenha(senha)
            )
        }

        val emailvalido = _loginScreenState.value.emailValido
        val senhaValida = _loginScreenState.value.senhaValida

        if (emailvalido && senhaValida) {

            _loginScreenState.update { currentState ->
                currentState.copy(
                    enabledButton = false,
                )
            }

            if (possuiConexao(ctx)) {
                val loginCliente = LoginCliente(email, senha)
                loginCliente.sendLoginData(object : LoginCliente.Callback {
                    override fun onSuccess(idUsuarioRecebido: String) {
                        // salvar o id do usuário logado
                        if (idUsuarioRecebido != "-1") {
                            viewModelScope.launch {
                                ctx.dataStore.edit { preferences ->
                                    preferences[usuarioLogadoPreferences] = idUsuarioRecebido
                                }
                                _idUsuario.value = idUsuarioRecebido
                            }
                        } else {
                            _loginScreenState.update { currentState ->
                                currentState.copy(
                                    enabledButton = true,
                                    dadosIncorretos = true
                                )
                            }
                        }
                    }

                    override fun onFailure(e: IOException) {
                        viewModelScope.launch {
                            Toast.makeText(ctx, R.string.erro_rede, Toast.LENGTH_SHORT).show()
                        }
                        _loginScreenState.update { currentState ->
                            currentState.copy(
                                enabledButton = true,
                            )
                        }
                    }
                })
            } else {
                Toast.makeText(
                    ctx,
                    R.string.verifique_conexao_internet,
                    Toast.LENGTH_SHORT
                )
                    .show()
                _loginScreenState.update { currentState ->
                    currentState.copy(
                        enabledButton = true,
                    )
                }
            }
        }
    }

    fun setClienteData(idUsuario: String, ctx: Context) {
        viewModelScope.launch {
            clienteLogado = getCliente(idUsuario.toInt())
            if (clienteLogado.idCliente == -1) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        ctx,
                        R.string.servidor_em_manutencao,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                (ctx as? Activity)?.finish()
            }
        }
    }

    fun resetLoginScreenState() {
        _loginScreenState.value = LoginScreenState()
    }
}
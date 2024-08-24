package com.ahpp.notshoes.viewModel.deslogado

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.ahpp.notshoes.R
import com.ahpp.notshoes.data.cliente.RegistroCliente
import com.ahpp.notshoes.states.deslogado.RegistroScreenState
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.util.validacao.ValidarCamposDados.validarEmail
import com.ahpp.notshoes.util.validacao.ValidarCamposDados.validarNome
import com.ahpp.notshoes.util.validacao.ValidarCamposDados.validarSenha
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.IOException

class RegistroScreenViewModel : ViewModel() {

    private val _registroScreenState = MutableStateFlow(RegistroScreenState())
    val registroScreenState: StateFlow<RegistroScreenState> = _registroScreenState.asStateFlow()

    fun setNome(nome: String) {
        _registroScreenState.update { currentState ->
            currentState.copy(
                nome = nome,
                nomeValido = validarNome(nome),
            )
        }
    }

    fun setEmail(email: String) {
        _registroScreenState.update { currentState ->
            currentState.copy(
                email = email,
                emailValido = validarEmail(email),
            )
        }
    }

    fun setSenha(senha: String) {
        _registroScreenState.update { currentState ->
            currentState.copy(
                senha = senha,
                senhaValida = validarSenha(senha),
            )
        }
    }

    fun setCodigoStatusRegistro() {
        _registroScreenState.update { currentState ->
            currentState.copy(
                codigoStatusRegistro = "-1"
            )
        }
    }

    fun sendRegistro(ctx: Context) {
        val nome = _registroScreenState.value.nome
        val email = _registroScreenState.value.email
        val senha = _registroScreenState.value.senha

        _registroScreenState.update { currentState ->
            currentState.copy(
                nomeValido = validarNome(nome),
                emailValido = validarEmail(email),
                senhaValida = validarSenha(senha)
            )
        }

        val nomeValido = _registroScreenState.value.nomeValido
        val emailValido = _registroScreenState.value.emailValido
        val senhaValida = _registroScreenState.value.senhaValida

        if (nomeValido && emailValido && senhaValida) {

            upDateButtonState(false)

            if (possuiConexao(ctx)) {
                val registroCliente = RegistroCliente(nome, email, senha)

                registroCliente.sendRegistroData(object : RegistroCliente.Callback {
                    override fun onSuccess(code: String) {
                        //500 = usuario ja existe
                        //201 = usuario criado com sucesso
                        _registroScreenState.update { currentState ->
                            currentState.copy(
                                codigoStatusRegistro = code
                            )
                        }
                        if (code == "500") {
                            upDateButtonState(true)
                        }
                    }

                    override fun onFailure(e: IOException) {
                        // cai aqui em caso de erro de rede
                        // não é possível mostrar um Toast de um Thread
                        // que não seja UI, então é feito dessa forma
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                ctx,
                                R.string.erro_rede,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        upDateButtonState(true)
                    }
                })
            } else {
                Toast.makeText(
                    ctx,
                    R.string.verifique_conexao_internet,
                    Toast.LENGTH_SHORT
                )
                    .show()
                upDateButtonState(true)
            }
        }
    }

    fun resetRegistroScreenState() {
        _registroScreenState.value = RegistroScreenState()
    }

    fun resetCodigoStatusRegistro() {
        _registroScreenState.update { currentState ->
            currentState.copy(
                codigoStatusRegistro = "-1"
            )
        }
    }

    private fun upDateButtonState(enabledButton: Boolean) {
        _registroScreenState.update { currentState ->
            currentState.copy(
                enabledButton = enabledButton,
            )
        }
    }
}
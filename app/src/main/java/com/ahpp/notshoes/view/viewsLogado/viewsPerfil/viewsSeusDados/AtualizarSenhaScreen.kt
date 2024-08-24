package com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.data.cliente.AtualizarSenhaCliente
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.util.validacao.ValidarCamposDados
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.constantes.clienteLogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.security.MessageDigest
import java.util.Locale

@Composable
fun AtualizarSenhaScreen(navControllerSeusDados: NavController) {
    val scope = rememberCoroutineScope()
    fun atualizarClienteLogado() {
        scope.launch(Dispatchers.IO) {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }

    val ctx = LocalContext.current

    var enabledButton by remember { mutableStateOf(true) }

    var senhaAtual by remember { mutableStateOf("") }
    var senhaNova by remember { mutableStateOf("") }

    var senhaAtualCorreta by remember { mutableStateOf(true) }
    var senhaAtualValida by remember { mutableStateOf(true) }
    var senhaNovaValida by remember { mutableStateOf(true) }

    var codigoStatusAlteracao by remember { mutableStateOf("201") }

    var senhaAtualVisibility by remember { mutableStateOf(false) }
    var senhaNovoVisibility by remember { mutableStateOf(false) }

    val iconVisibility1 = if (senhaAtualVisibility)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

    val iconVisibility2 = if (senhaNovoVisibility)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

    val colorsTextFields = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFEEF3F5),
        focusedContainerColor = Color(0xFFEEF3F5),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color.DarkGray,
        focusedLabelColor = Color(0xFF000000),
        cursorColor = Color(0xFF029CCA),
        errorContainerColor = Color(0xFFEEF3F5),
        errorSupportingTextColor = Color(0xFFC00404)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(azulEscuro)
                .padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .size(45.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    if (navControllerSeusDados.canGoBack) {
                        navControllerSeusDados.popBackStack("seusDadosScreen", false)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = stringResource(id = R.string.toque_para_voltar_description),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(azulEscuro),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.padding(top = 70.dp))
            Image(
                painter = painterResource(id = R.drawable.baseline_key_24),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = stringResource(id = R.string.atualizar_senha),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            OutlinedTextField(
                value = senhaAtual,
                onValueChange = {
                    if (it.length <= 255) {
                        senhaAtual = it
                    }
                    senhaAtualValida = ValidarCamposDados.validarSenha(senhaAtual)
                    senhaAtualCorreta = true
                },
                isError = !senhaAtualValida || !senhaAtualCorreta,
                supportingText = {
                    if (!senhaAtualValida) {
                        Text(
                            text = stringResource(id = R.string.digite_senha_valida),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    } else if (!senhaAtualCorreta) {
                        Text(
                            text = stringResource(R.string.senha_atual_incorreta),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.senha_atual),
                        color = corPlaceholder
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        senhaAtualVisibility = !senhaAtualVisibility
                    }) {
                        Icon(
                            painter = iconVisibility1,
                            contentDescription = stringResource(id = R.string.alterar_visibilidade_senha),
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (senhaAtualVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 20.dp),
                colors = colorsTextFields
            )
            OutlinedTextField(
                value = senhaNova,
                onValueChange = {
                    if (it.length <= 255) {
                        senhaNova = it
                    }
                    senhaNovaValida = ValidarCamposDados.validarSenha(senhaNova)
                },
                isError = !senhaNovaValida,
                supportingText = {
                    if (!senhaNovaValida) {
                        Text(
                            text = stringResource(id = R.string.digite_senha_valida),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.nova_senha),
                        color = corPlaceholder
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        senhaNovoVisibility = !senhaNovoVisibility
                    }) {
                        Icon(
                            painter = iconVisibility2,
                            contentDescription = stringResource(id = R.string.alterar_visibilidade_senha),
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (senhaNovoVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                colors = colorsTextFields
            )

            Spacer(Modifier.padding(top = 10.dp))

            ElevatedButton(
                onClick = {

                    val senhaAtualCriptografada: String = md5Hash(senhaAtual)

                    senhaAtualCorreta = senhaAtualCriptografada == clienteLogado.senha

                    senhaAtualValida = ValidarCamposDados.validarSenha(senhaAtual)
                    senhaNovaValida = ValidarCamposDados.validarSenha(senhaNova)

                    if (senhaAtualCorreta && senhaAtualValida && senhaNovaValida) {
                        enabledButton = false
                        if (possuiConexao(ctx)) {
                            val atualizarSenhaCliente = AtualizarSenhaCliente(senhaNova)

                            atualizarSenhaCliente.sendAtualizarData(object :
                                AtualizarSenhaCliente.Callback {
                                override fun onSuccess(code: String) {
                                    //500 = erro
                                    //201 = senha alterada com sucesso
                                    codigoStatusAlteracao = code
                                    //Log.i("CODIGO RECEBIDO {ALTERAR SENHA}: ", code)

                                    if (code == "201") {
                                        atualizarClienteLogado()
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                R.string.senha_alterada_com_sucesso,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navControllerSeusDados.popBackStack(
                                                "seusDadosScreen",
                                                false
                                            )
                                        }
                                    }
                                }

                                override fun onFailure(e: IOException) {
                                    // erro de rede
                                    // não é possível mostrar um Toast de um Thread
                                    // que não seja UI, então é feito dessa forma
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(ctx, R.string.erro_rede, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    //Log.e("Erro: ", e.message.toString())
                                    enabledButton = true
                                }
                            })
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    ctx,
                                    R.string.erro_rede,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            enabledButton = true
                        }
                    }
                },
                modifier = Modifier
                    .width(230.dp)
                    .height(50.dp),
                enabled = enabledButton,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = stringResource(id = R.string.botao_atualizar),
                    fontSize = 15.sp,
                    color = azulEscuro
                )
            }
            Spacer(Modifier.padding(top = 10.dp))
            Text(
                modifier = Modifier.clickable(true) {
                    if (navControllerSeusDados.canGoBack) {
                        navControllerSeusDados.popBackStack("seusDadosScreen", false)
                    }
                },
                text = stringResource(id = R.string.cancelar).uppercase(Locale.ROOT),
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

fun md5Hash(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bytes = md.digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
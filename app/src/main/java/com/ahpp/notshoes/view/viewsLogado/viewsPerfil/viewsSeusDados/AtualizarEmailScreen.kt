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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.ColorsTextFieldAtualizarEmailSenha
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.AtualizarEmailCliente
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.util.validacao.ValidarCamposDados
import com.ahpp.notshoes.viewModel.logado.perfil.seusDados.AtualizarEmailScreenViewModel
import java.io.IOException
import java.util.Locale

@Composable
fun AtualizarEmailScreen(
    navControllerSeusDados: NavController,
    atualizarEmailScreenViewModel: AtualizarEmailScreenViewModel = viewModel()
) {

    val uiState = atualizarEmailScreenViewModel.atualizarEmailScreenState.collectAsState()
    val emailNovo = uiState.value.emailNovo

    val ctx = LocalContext.current
    var enabledButton by remember { mutableStateOf(true) }
    var emailValido by remember { mutableStateOf(true) }

    var codigoStatusAlteracao by remember { mutableStateOf("201") }

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
                painter = painterResource(id = R.drawable.baseline_contact_mail_24),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = stringResource(id = R.string.atualizar_email),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                enabled = false,
                placeholder = { Text(clienteLogado.email, color = corPlaceholder) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFEEF3F5),
                )
            )
            Spacer(Modifier.padding(top = 15.dp))
            OutlinedTextField(
                value = emailNovo,
                onValueChange = {
                    if (it.length <= 255) {
                        atualizarEmailScreenViewModel.setNovoEmail(it)
                    }
                    emailValido = ValidarCamposDados.validarEmail(emailNovo)
                    codigoStatusAlteracao = "201"
                },
                isError = !emailValido || codigoStatusAlteracao == "500",
                supportingText = {
                    if (!emailValido) {
                        Text(
                            text = stringResource(id = R.string.digite_email_valido),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    } else if (codigoStatusAlteracao == "500") {
                        Text(text = stringResource(id = R.string.email_ja_cadastrado))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.digite_um_novo_e_mail),
                        color = corPlaceholder
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                colors = ColorsTextFieldAtualizarEmailSenha.colorsTextField()
            )

            Spacer(Modifier.padding(top = 10.dp))

            ElevatedButton(
                onClick = {

                    emailValido = ValidarCamposDados.validarEmail(emailNovo)

                    if (emailValido) {
                        enabledButton = false
                        if (possuiConexao(ctx)) {
                            val atualizarEmailCliente =
                                AtualizarEmailCliente(emailNovo)
                            atualizarEmailCliente.sendAtualizarData(object :
                                AtualizarEmailCliente.Callback {
                                override fun onSuccess(code: String) {
                                    //500 = email ja existe
                                    //201 = email alterado com sucesso
                                    codigoStatusAlteracao = code
                                    //Log.i("CODIGO RECEBIDO {ALTERAR EMAIL}: ", code)

                                    if (code == "201") {
                                        atualizarEmailScreenViewModel.atualizarClienteLogado()
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                R.string.e_mail_alterado_com_sucesso,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navControllerSeusDados.popBackStack(
                                                "seusDadosScreen",
                                                false
                                            )
                                        }
                                    } else if (code == "500") {
                                        enabledButton = true
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
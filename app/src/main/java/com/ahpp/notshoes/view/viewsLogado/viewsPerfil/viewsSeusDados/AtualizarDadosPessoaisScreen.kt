package com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsSeusDados

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.data.cliente.AtualizarDadosPessoaisCliente
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.util.RadioButtonButtonPersonalizado
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.util.validacao.ValidarCamposDados
import com.ahpp.notshoes.util.funcoes.conexao.possuiConexao
import com.ahpp.notshoes.util.visualTransformation.CpfVisualTransformation
import com.ahpp.notshoes.util.visualTransformation.PhoneVisualTransformation
import com.ahpp.notshoes.constantes.clienteLogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun AtualizarDadosPessoaisScreen(navControllerSeusDados: NavController) {
    val scope = rememberCoroutineScope()
    fun atualizarClienteLogado() {
        scope.launch(Dispatchers.IO) {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }

    val ctx = LocalContext.current

    var enabledButton by remember { mutableStateOf(true) }

    var nomeNovo by remember { mutableStateOf(clienteLogado.nome) }
    var cpfNovo by remember { mutableStateOf(clienteLogado.cpf) }
    var telefoneNovo by remember { mutableStateOf(clienteLogado.telefoneContato) }
    var generoNovo by remember { mutableStateOf(clienteLogado.genero) }

    var nomeValido by remember { mutableStateOf(true) }
    var cpfValido by remember { mutableStateOf(true) }
    var telefoneValido by remember { mutableStateOf(true) }

    val colorsTextField = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFEEF3F5),
        focusedContainerColor = Color(0xFFEEF3F5),
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        unfocusedBorderColor = Color(0xFFEEF3F5),
        focusedBorderColor = Color(0xFF029CCA),
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
                        navControllerSeusDados.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.toque_para_voltar_description),
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.atualizar_dados_pessoais),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                Text(
                    text = stringResource(R.string.nome),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedTextField(
                value = nomeNovo,
                onValueChange = {
                    if (it.length <= 255) {
                        nomeNovo = it
                    }
                    nomeValido = true
                },
                isError = !nomeValido,
                supportingText = {
                    if (!nomeValido) {
                        Text(text = stringResource(R.string.digite_nome_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.nome),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField
            )

            Row(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                Text(
                    text = stringResource(R.string.cpf),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedTextField(
                value = if (cpfNovo == "") "" else cpfNovo,
                onValueChange = {
                    if (it.length <= 11) {
                        cpfNovo = it
                    }
                    cpfValido = true
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = !cpfValido,
                supportingText = {
                    if (!cpfValido) {
                        Text(text = stringResource(R.string.digite_um_cpf_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.digite_seu_cpf),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField,
                visualTransformation = CpfVisualTransformation()
            )

            Row(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                Text(
                    text = stringResource(R.string.telefone_de_contato),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedTextField(
                value = if (telefoneNovo == "") "" else telefoneNovo,
                onValueChange = {
                    if (it.length < 12) {
                        telefoneNovo = it
                    }
                    telefoneValido = true
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = !telefoneValido,
                supportingText = {
                    if (!telefoneValido) {
                        Text(text = stringResource(R.string.digite_um_telefone_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.digite_seu_telefone_de_contato),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField,
                visualTransformation = PhoneVisualTransformation()
            )

            Row(modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
                Text(
                    text = stringResource(R.string.genero),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                RadioButtonButtonPersonalizado(
                    text = "Masculino",
                    isSelected = generoNovo == "M",
                    onClick = { generoNovo = "M" }
                )
                RadioButtonButtonPersonalizado(
                    text = "Feminino",
                    isSelected = generoNovo == "F",
                    onClick = { generoNovo = "F" }
                )
                RadioButtonButtonPersonalizado(
                    text = "Prefiro não informar",
                    isSelected = generoNovo == "0",
                    onClick = { generoNovo = "0" }
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 25.dp)
            ) {
                ElevatedButton(
                    onClick = {
                        nomeValido = ValidarCamposDados.validarNome(nomeNovo)
                        cpfValido = ValidarCamposDados.validarCpf(cpfNovo)
                        telefoneValido = ValidarCamposDados.validarTelefone(telefoneNovo)

                        if (nomeValido && cpfValido && telefoneValido) {
                            enabledButton = false
                            if (possuiConexao(ctx)) {
                                val atualizarDadosCliente =
                                    AtualizarDadosPessoaisCliente(
                                        nomeNovo,
                                        cpfNovo,
                                        telefoneNovo,
                                        generoNovo
                                    )

                                atualizarDadosCliente.sendAtualizarData(object :
                                    AtualizarDadosPessoaisCliente.Callback {
                                    override fun onSuccess(code: String) {
                                        //Log.i("CODIGO RECEBIDO{ALTERAR DADOS CLIENTE}: ", code)
                                        atualizarClienteLogado()
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                R.string.dados_foram_atualizados,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navControllerSeusDados.popBackStack()
                                        }
                                    }

                                    override fun onFailure(e: IOException) {
                                        // erro de rede
                                        // não é possível mostrar um Toast de um Thread
                                        // que não seja UI, então é feito dessa forma
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                R.string.erro_rede,
                                                Toast.LENGTH_SHORT
                                            )
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
                    colors = ButtonDefaults.buttonColors(containerColor = azulEscuro)
                ) {
                    Text(
                        text = stringResource(R.string.botao_atualizar),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
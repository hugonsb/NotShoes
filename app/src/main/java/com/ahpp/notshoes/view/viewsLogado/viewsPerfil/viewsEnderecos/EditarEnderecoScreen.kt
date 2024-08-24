package com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.data.endereco.EditarEnderecoCliente
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.util.validacao.ValidarCamposEndereco
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.util.visualTransformation.CepVisualTransformation
import com.ahpp.notshoes.constantes.clienteLogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarEnderecoScreen(navControllerEnderecos: NavController) {

    val scope = rememberCoroutineScope()
    fun atualizarClienteLogado() {
        scope.launch(Dispatchers.IO) {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }

    val ctx = LocalContext.current

    var enabledButton by remember { mutableStateOf(true) }

    var cep by remember { mutableStateOf(enderecoSelecionado.cep) }
    var endereco by remember { mutableStateOf(enderecoSelecionado.endereco) }
    var numero by remember { mutableStateOf(enderecoSelecionado.numero.toString()) }
    var complemento by remember { mutableStateOf(enderecoSelecionado.complemento) }
    var bairro by remember { mutableStateOf(enderecoSelecionado.bairro) }
    var cidade by remember { mutableStateOf(enderecoSelecionado.cidade) }

    var cepValido by remember { mutableStateOf(true) }
    var enderecoValido by remember { mutableStateOf(true) }
    var numeroValido by remember { mutableStateOf(true) }
    var bairroValido by remember { mutableStateOf(true) }
    var estadoValido by remember { mutableStateOf(true) }
    var cidadeValido by remember { mutableStateOf(true) }

    val estadosList = listOf(
        "Estado",
        "Acre",
        "Alagoas",
        "Amapá",
        "Amazonas",
        "Bahia",
        "Ceará",
        "Distrito Federal",
        "Espírito Santo",
        "Goiás",
        "Maranhão",
        "Mato Grosso",
        "Mato Grosso do Sul",
        "Minas Gerais",
        "Pará",
        "Paraíba",
        "Paraná",
        "Pernambuco",
        "Piauí",
        "Rio de Janeiro",
        "Rio Grande do Norte",
        "Rio Grande do Sul",
        "Rondônia",
        "Roraima",
        "Santa Catarina",
        "São Paulo",
        "Sergipe",
        "Tocantins"
    )
    var expanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf(estadosList[estadosList.indexOf(enderecoSelecionado.estado)]) }

    var checkedTornarEnderecoPrincipal by remember { mutableStateOf(false) }

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
                    if (navControllerEnderecos.canGoBack) {
                        navControllerEnderecos.popBackStack("enderecosScreen", false)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Image(
                    Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.toque_para_voltar_description),
                    modifier = Modifier.size(30.dp)
                )
            }

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = stringResource(R.string.editar_enredeco),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = cep,
                onValueChange = {
                    if (it.length <= 8) {
                        cep = it
                    }
                    cepValido = true
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = !cepValido,
                supportingText = {
                    if (!cepValido) {
                        Text(text = stringResource(id = R.string.digite_um_cep_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.cep),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField,
                visualTransformation = CepVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = endereco,
                onValueChange = {
                    if (it.length <= 255) {
                        endereco = it
                    }
                    enderecoValido = true
                },
                isError = !enderecoValido,
                supportingText = {
                    if (!enderecoValido) {
                        Text(text = stringResource(id = R.string.digite_um_endereco_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.endereco),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                OutlinedTextField(
                    value = numero,
                    onValueChange = {
                        if (it.length <= 10) {
                            numero = it
                        }
                        numeroValido = true
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = !numeroValido,
                    supportingText = {
                        if (!numeroValido) {
                            Text(text = stringResource(id = R.string.digite_um_numero_valido))
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.numero),
                            color = corPlaceholder
                        )
                    },
                    modifier = Modifier
                        .padding(start = 10.dp, end = 5.dp)
                        .weight(1f),
                    maxLines = 1,
                    colors = colorsTextField
                )

                OutlinedTextField(
                    value = complemento,
                    onValueChange = {
                        if (it.length <= 255) {
                            complemento = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.complemento),
                            color = corPlaceholder
                        )
                    },
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp)
                        .weight(1f),
                    maxLines = 1,
                    colors = colorsTextField
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = bairro,
                onValueChange = {
                    if (it.length <= 255) {
                        bairro = it
                    }
                    bairroValido = true
                },
                isError = !bairroValido,
                supportingText = {
                    if (!bairroValido) {
                        Text(text = stringResource(id = R.string.informe_um_bairro_valido))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.bairro),
                        color = corPlaceholder
                    )
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                colors = colorsTextField
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 5.dp)
                        .weight(1f),
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {

                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = estado,
                        onValueChange = {},
                        isError = !estadoValido,
                        supportingText = {
                            if (!estadoValido) {
                                Text(text = stringResource(id = R.string.escolha_um_estado))
                            }
                        },
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = colorsTextField
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        estadosList.forEach { estadoSelecionado ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        estadoSelecionado,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    estado = estadoSelecionado
                                    expanded = false
                                    estadoValido = true
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = cidade,
                    onValueChange = {
                        if (it.length <= 255) {
                            cidade = it
                        }
                        cidadeValido = true
                    },
                    isError = !cidadeValido,
                    supportingText = {
                        if (!cidadeValido) {
                            Text(text = stringResource(id = R.string.informe_uma_cidade_valida))
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.cidade),
                            color = corPlaceholder
                        )
                    },
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp)
                        .weight(1f),
                    maxLines = 1,
                    colors = colorsTextField
                )
            }

            if (enderecoSelecionado.idEndereco != clienteLogado.idEnderecoPrincipal) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedTornarEnderecoPrincipal,
                        onCheckedChange = { checkedTornarEnderecoPrincipal = it },
                        colors = CheckboxDefaults.colors(azulEscuro)
                    )
                    Text(
                        text = stringResource(R.string.tornar_esse_endereco_principal),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedButton(
                    onClick = {
                        cepValido = ValidarCamposEndereco.validarCep(cep)
                        enderecoValido = ValidarCamposEndereco.validarEndereco(endereco)
                        numeroValido = ValidarCamposEndereco.validarNumero(numero)
                        bairroValido = ValidarCamposEndereco.validarBairro(bairro)
                        estadoValido = ValidarCamposEndereco.validarEstado(estado)
                        cidadeValido = ValidarCamposEndereco.validarCidade(cidade)

                        if (cepValido && enderecoValido && numeroValido && bairroValido && estadoValido && cidadeValido) {
                            enabledButton = false
                            if (possuiConexao(ctx)) {
                                val editarEnderecoCliente =
                                    EditarEnderecoCliente(
                                        estado,
                                        cidade,
                                        cep,
                                        endereco,
                                        bairro,
                                        numero,
                                        complemento,
                                        clienteLogado.idCliente,
                                        enderecoSelecionado.idEndereco,
                                        checkedTornarEnderecoPrincipal
                                    )

                                editarEnderecoCliente.sendEditarEnderecoCliente(object :
                                    EditarEnderecoCliente.Callback {
                                    override fun onSuccess(code: String) {
                                        //Log.i("CODIGO RECEBIDO (sucesso na atualizacao de endereço): ", code)
                                        if (code == "1") {
                                            if (checkedTornarEnderecoPrincipal) {
                                                atualizarClienteLogado()
                                            }
                                            Handler(Looper.getMainLooper()).post {
                                                Toast.makeText(
                                                    ctx,
                                                    R.string.endereco_atualizado_com_sucesso,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navControllerEnderecos.popBackStack(
                                                    "enderecosScreen",
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
                    colors = ButtonDefaults.buttonColors(containerColor = azulEscuro),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.editar_enredeco),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(
                            Color.White
                        )
                    )
                }
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    modifier = Modifier.clickable(true) {
                        if (navControllerEnderecos.canGoBack) {
                            navControllerEnderecos.popBackStack("enderecosScreen", false)
                        }
                    },
                    text = stringResource(R.string.cancelar).uppercase(Locale.ROOT),
                    fontSize = 15.sp,
                    style = TextStyle(
                        Color.Black
                    )
                )
            }
        }
    }
}
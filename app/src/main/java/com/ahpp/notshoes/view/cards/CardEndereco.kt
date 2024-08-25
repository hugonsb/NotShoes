package com.ahpp.notshoes.view.cards

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.cliente.getCliente
import com.ahpp.notshoes.data.endereco.RemoverEnderecoCliente
import com.ahpp.notshoes.model.Endereco
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.util.visualTransformation.CepVisualTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun CardEndereco(
    onClickEditarEndereco: () -> Unit,
    enderecoCliente: Endereco,
    onRemoveEndereco: () -> Unit
) {

    val scope = rememberCoroutineScope()
    fun atualizarClienteLogado() {
        scope.launch(Dispatchers.IO) {
            clienteLogado =
                getCliente(clienteLogado.idCliente)
        }
    }

    val ctx = LocalContext.current

    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardColors(containerColor = Color.White, Color.Black, Color.Black, Color.Black),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(340.dp)
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {

                if (clienteLogado.idEnderecoPrincipal == enderecoCliente.idEndereco) {
                    Text(
                        text = "Endereço principal",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = enderecoCliente.endereco,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${enderecoCliente.bairro} - ${enderecoCliente.cidade}",
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = enderecoCliente.estado,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val visualTransformation = CepVisualTransformation()
                val transformedText =
                    visualTransformation.filter(AnnotatedString(enderecoCliente.cep)).text

                Text(
                    text = "CEP: $transformedText",
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 8.dp, end = 10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween

            ) {
                Button(
                    modifier = Modifier.size(30.dp), contentPadding = PaddingValues(0.dp),
                    onClick = {
                        if (possuiConexao(ctx)) {
                            val repository = RemoverEnderecoCliente(
                                enderecoCliente.idEndereco,
                                clienteLogado.idCliente
                            )
                            repository.sendRemoverEnderecoCliente(object :
                                RemoverEnderecoCliente.Callback {
                                override fun onSuccess(code: String) {
                                    Log.i(
                                        "CODIGO RECEBIDO (sucesso na remocao do endereço): ",
                                        code
                                    )

                                    if (code == "1") {
                                        atualizarClienteLogado()
                                        onRemoveEndereco()
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                ctx,
                                                "Endereço removido.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        Log.e("Erro ao remover endereço, codigo recebido: ", code)
                                    }
                                }

                                override fun onFailure(e: IOException) {
                                    // erro de rede
                                    // não é possível mostrar um Toast de um Thread
                                    // que não seja UI, então é feito dessa forma
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    Log.e("Erro ao remover endereço: ", e.message.toString())
                                }
                            })
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    elevation = ButtonDefaults.buttonElevation(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Remover endereço.",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Button(
                    modifier = Modifier.size(30.dp), contentPadding = PaddingValues(0.dp),
                    onClick = {
                        onClickEditarEndereco()
                    },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    elevation = ButtonDefaults.buttonElevation(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_edit_24),
                        contentDescription = "Editar endereço.",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
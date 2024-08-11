package com.ahpp.notshoes.view.viewsDeslogado

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.CustomTextFieldColors
import com.ahpp.notshoes.states.deslogado.RegistroScreenState
import com.ahpp.notshoes.ui.theme.azulClaro
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.navigation.canGoBack
import com.ahpp.notshoes.viewModel.deslogado.RegistroScreenViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    registroScreenViewModel: RegistroScreenViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        registroScreenViewModel.resetRegistroScreenState()
    }

    val uiRegistroScreenState by registroScreenViewModel.registroScreenState.collectAsState()

    RegistroContent(uiRegistroScreenState, registroScreenViewModel, navController)
}

@Composable
fun RegistroContent(
    uiRegistroScreenState: RegistroScreenState,
    registroScreenViewModel: RegistroScreenViewModel,
    navController: NavController
) {
    val ctx = LocalContext.current

    val codigoStatusRegistro = uiRegistroScreenState.codigoStatusRegistro

    if (codigoStatusRegistro == "201") {
        registroScreenViewModel.resetCodigoStatusRegistro()
        Toast.makeText(
            ctx,
            R.string.conta_criada,
            Toast.LENGTH_SHORT
        ).show()
        navController.popBackStack("login", false)
    }

    val nome = uiRegistroScreenState.nome
    val nomeValido = uiRegistroScreenState.nomeValido

    val email = uiRegistroScreenState.email
    val emailValido = uiRegistroScreenState.emailValido

    val senha = uiRegistroScreenState.senha
    val senhaValida = uiRegistroScreenState.senhaValida

    val enabledButton = uiRegistroScreenState.enabledButton

    var checkedTermos by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

    val shapeArredondado = RoundedCornerShape(10.dp)
    val elevationButton = ButtonDefaults.buttonElevation(5.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color.White,
                        Color.White,
                        Color.White,
                        Color.White,
                        azulClaro,
                        azulEscuro
                    )
                )
            )
            .padding(start = 20.dp, end = 20.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(150.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.crie_conta_aproveite_ofertas),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = azulEscuro
            )
        }

        OutlinedTextField(
            value = nome,
            onValueChange = {
                if (it.length <= 255) {
                    registroScreenViewModel.setNome(it)
                }
            },
            isError = !nomeValido,
            supportingText = {
                if (!nomeValido) {
                    Text(text = stringResource(id = R.string.digite_nome_valido))
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.nome),
                    color = corPlaceholder
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = null, tint = Color.Black)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            maxLines = 1,
            colors = CustomTextFieldColors.colorsTextFields(),
            shape = shapeArredondado
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                if (it.length <= 255) {
                    registroScreenViewModel.setEmail(it)
                    registroScreenViewModel.setCodigoStatusRegistro()
                }
            },
            isError = !emailValido || codigoStatusRegistro == "500",
            supportingText = {
                if (!emailValido) {
                    Text(text = stringResource(id = R.string.digite_email_valido))
                } else if (codigoStatusRegistro == "500") {
                    Text(text = stringResource(id = R.string.email_ja_cadastrado))
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.email),
                    color = corPlaceholder
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = Color.Black)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            maxLines = 1,
            colors = CustomTextFieldColors.colorsTextFields(),
            shape = shapeArredondado
        )

        OutlinedTextField(
            value = senha,
            isError = !senhaValida,
            supportingText = {
                if (!senhaValida) {
                    Text(text = stringResource(id = R.string.digite_senha_valida))
                }
            },
            onValueChange = {
                if (it.length <= 255) {
                    registroScreenViewModel.setSenha(it)
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.senha),
                    color = corPlaceholder
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = icon,
                        contentDescription = stringResource(id = R.string.alterar_visibilidade_senha),
                        tint = Color.Black
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),

            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            maxLines = 1,
            colors = CustomTextFieldColors.colorsTextFields(),
            shape = shapeArredondado
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedTermos,
                onCheckedChange = { checkedTermos = it },
                colors = CheckboxDefaults.colors(azulEscuro)
            )
            Row {
                Text(
                    text = stringResource(id = R.string.li_concordo_com) + " ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 15.sp
                )
                Text(
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = { }),
                    text = stringResource(id = R.string.termos_uso),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = azulEscuro
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            FilledTonalButton(
                onClick = {
                    if (checkedTermos) {
                        registroScreenViewModel.sendRegistro(ctx)
                    } else {
                        Toast.makeText(ctx, R.string.aceite_termos_uso, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = enabledButton,
                colors = ButtonDefaults.buttonColors(containerColor = azulEscuro),
                shape = shapeArredondado,
                elevation = elevationButton
            ) {
                Text(
                    text = stringResource(id = R.string.criar_conta),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        Color.White
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 25.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.ja_possui_conta) + " ",
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyLarge,
                color =
                Color.Black

            )
            Text(
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        if (navController.canGoBack) {
                            navController.popBackStack("login", false)
                        }
                    }),
                text = stringResource(id = R.string.entre),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = azulEscuro
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Image(
                painter = painterResource(id = R.drawable.img_registro),
                contentDescription = null,
                modifier = Modifier
                    .width(185.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 3.dp)
            )
        }

    }
}
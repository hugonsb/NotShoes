package com.ahpp.notshoes.view.viewsDeslogado

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import com.ahpp.notshoes.R
import com.ahpp.notshoes.constantes.CustomTextFieldColors
import com.ahpp.notshoes.states.deslogado.LoginScreenState
import com.ahpp.notshoes.ui.theme.azulClaro
import com.ahpp.notshoes.ui.theme.azulEscuro
import com.ahpp.notshoes.ui.theme.corPlaceholder
import com.ahpp.notshoes.view.screensReutilizaveis.LoadingScreen
import com.ahpp.notshoes.util.conexao.possuiConexao
import com.ahpp.notshoes.viewModel.deslogado.LoginScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel = koinViewModel<LoginScreenViewModel>()
) {
    // resetar o estado da tela quando o usuario fazer logout
    LaunchedEffect(Unit) {
        loginScreenViewModel.resetLoginScreenState()
    }

    val uiState by loginScreenViewModel.loginScreenState.collectAsState()
    val idUsuario by loginScreenViewModel.idUsuario.collectAsState()

    val ctx = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(idUsuario) {
        when (idUsuario) {
            // nao tem usuario logado
            "-1" -> {
                isLoading = false
            }
            // esta carregando o id do banco local
            "-2" -> {
                isLoading = true
            }
            // ja tem um usuario logado
            else -> {
                if (possuiConexao(ctx)) {
                    scope.launch(Dispatchers.IO) {
                        loginScreenViewModel.setClienteData(idUsuario, ctx)
                        withContext(Dispatchers.Main) { // Switch back to main thread for UI update
                            navController.navigate("bottomNavBar") { launchSingleTop = true }
                        }
                    }
                } else {
                    Toast.makeText(ctx, R.string.verifique_conexao_internet, Toast.LENGTH_SHORT)
                        .show()
                    (ctx as? Activity)?.finish()
                }
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        LoginContent(uiState, loginScreenViewModel, ctx, navController)
    }
}

@Composable
fun LoginContent(
    uiLoginScreenState: LoginScreenState,
    loginScreenViewModel: LoginScreenViewModel,
    ctx: Context,
    navController: NavController
) {
    val email = uiLoginScreenState.email
    val emailValido = uiLoginScreenState.emailValido

    val senha = uiLoginScreenState.senha
    val senhaValida = uiLoginScreenState.senhaValida

    val dadosIncorretos = uiLoginScreenState.dadosIncorretos
    val enabledButton = uiLoginScreenState.enabledButton

    val shapeArredondado = RoundedCornerShape(10.dp)
    val elevationButton = ButtonDefaults.buttonElevation(10.dp)

    //gerenciar visibilidade da senha
    var passwordVisibility by remember { mutableStateOf(false) }

    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.baseline_visibility_24)
    else
        painterResource(id = R.drawable.baseline_visibility_off_24)

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
                text = stringResource(id = R.string.entre_aproveite_ofertas),
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = azulEscuro
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                if (it.length <= 255) {
                    loginScreenViewModel.setEmail(it)
                }
            },
            isError = !emailValido || (dadosIncorretos),
            supportingText = {
                if (!emailValido) {
                    Text(text = stringResource(id = R.string.digite_email_valido))
                } else if (dadosIncorretos) {
                    Text(text = stringResource(id = R.string.dados_incorretos))
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
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = CustomTextFieldColors.colorsTextFields(),
            shape = shapeArredondado
        )

        OutlinedTextField(
            value = senha,
            isError = !senhaValida || (dadosIncorretos),
            supportingText = {
                if (!senhaValida) {
                    Text(text = stringResource(id = R.string.digite_senha_valida))
                } else if (dadosIncorretos) {
                    Text(text = stringResource(id = R.string.dados_incorretos))
                }
            },
            onValueChange = {
                if (it.length <= 255) {
                    loginScreenViewModel.setSenha(it)
                }
            },
            placeholder = { Text(stringResource(id = R.string.senha), color = corPlaceholder) },
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
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            colors = CustomTextFieldColors.colorsTextFields(),
            shape = shapeArredondado
        )

        Row(
            modifier = Modifier
                .padding(top = 13.dp)
                .align(Alignment.End)
        ) {
            Text(
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        Toast.makeText(
                            ctx,
                            "Implementar isso aqui vai ser doidera",
                            Toast.LENGTH_SHORT
                        ).show()
                    }),
                text = stringResource(id = R.string.esqueceu_sua_senha),
                fontSize = 16.sp,
                color = azulEscuro
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 25.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    loginScreenViewModel.sendLogin(ctx)
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
                    text = stringResource(id = R.string.entrar),
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
                text = stringResource(id = R.string.nao_tem_conta) + " ",
                fontSize = 15.sp,
                style = TextStyle(
                    Color.Black
                )
            )
            Text(
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        navController.navigate("registro") {
                            launchSingleTop = true
                        }
                    }),
                text = stringResource(id = R.string.cadastrese),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = azulEscuro
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Image(
                painter = painterResource(id = R.drawable.img_login),
                contentDescription = null,
                modifier = Modifier
                    .width(250.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 3.dp)
            )
        }
    }
}
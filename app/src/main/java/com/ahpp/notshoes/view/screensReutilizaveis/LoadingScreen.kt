package com.ahpp.notshoes.view.screensReutilizaveis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ahpp.notshoes.ui.theme.azulEscuro

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = azulEscuro)
    }
}
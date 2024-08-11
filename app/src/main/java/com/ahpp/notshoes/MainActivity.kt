package com.ahpp.notshoes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ahpp.notshoes.navigation.deslogado.NavManagerDeslogado

//usado para salvar o id do usuario logado, precisa ser definido no level mais alto do projeto
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_id_logado")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            NavManagerDeslogado()
        }
    }
}
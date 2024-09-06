package com.ahpp.notshoes.di

import com.ahpp.notshoes.data.produto.ProdutoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.ahpp.notshoes.viewModel.deslogado.LoginScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.ahpp.notshoes.viewModel.logado.inicio.PromocoesInicioScreenViewModel
import com.ahpp.notshoes.viewModel.screensReutilizaveis.ResultadosScreenViewModel
import com.ahpp.notshoes.viewModel.logado.listaDesejos.ListaDesejosViewModel
import com.ahpp.notshoes.viewModel.screensReutilizaveis.ProdutoScreenViewModel

val appModule = module {
    viewModel { LoginScreenViewModel(androidContext()) }
    viewModelOf(::PromocoesInicioScreenViewModel)
    viewModelOf(::ResultadosScreenViewModel)
    viewModelOf(::ListaDesejosViewModel)
    viewModelOf(::ProdutoScreenViewModel)
}

val dbModule = module {
    single { ProdutoRepository() }
}
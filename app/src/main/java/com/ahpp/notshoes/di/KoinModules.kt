package com.ahpp.notshoes.di

import com.ahpp.notshoes.data.produto.ProdutoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.ahpp.notshoes.viewModel.deslogado.LoginScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.ahpp.notshoes.viewModel.logado.inicio.PromocoesInicioScreenViewModel

val appModule = module {
    viewModel { LoginScreenViewModel(androidContext()) }
    viewModelOf(::PromocoesInicioScreenViewModel)
}

val dbModule = module {
    single { ProdutoRepository() }
}
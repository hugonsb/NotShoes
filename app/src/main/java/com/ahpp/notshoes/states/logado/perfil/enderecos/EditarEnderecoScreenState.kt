package com.ahpp.notshoes.states.logado.perfil.enderecos

import com.ahpp.notshoes.view.viewsLogado.viewsPerfil.viewsEnderecos.enderecoSelecionado

data class EditarEnderecoScreenState (
    val cep: String = enderecoSelecionado.cep,
    val endereco: String = enderecoSelecionado.endereco,
    val numero: String = enderecoSelecionado.numero.toString(),
    val bairro: String = enderecoSelecionado.bairro,
    val cidade: String = enderecoSelecionado.cidade,
    val complemento: String = enderecoSelecionado.complemento
)
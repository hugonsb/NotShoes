package com.ahpp.notshoes.states.logado.perfil.enderecos

data class CadastrarEnderecoScreenState (
    val cep: String = "",
    val endereco: String = "",
    val numero: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val complemento: String = ""
)
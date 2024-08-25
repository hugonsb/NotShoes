package com.ahpp.notshoes.states.logado.perfil.seusDados

import com.ahpp.notshoes.constantes.clienteLogado

data class AtualizarDadosPessoaisScreenState (
    val nomeNovo: String = clienteLogado.nome,
    val cpfNovo: String = clienteLogado.cpf,
    val telefoneContatoNovo: String = clienteLogado.telefoneContato,
    val generoNovo: String = clienteLogado.genero,
)
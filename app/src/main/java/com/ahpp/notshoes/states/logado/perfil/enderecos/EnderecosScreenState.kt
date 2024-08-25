package com.ahpp.notshoes.states.logado.perfil.enderecos

import com.ahpp.notshoes.model.Endereco

data class EnderecosScreenState (
    val enderecosList: List<Endereco> = emptyList(),
    val isLoading: Boolean = true
)
package com.ahpp.notshoes.util.funcoes.produto

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ahpp.notshoes.data.carrinho.CarrinhoRepository
import com.ahpp.notshoes.model.Produto
import com.ahpp.notshoes.constantes.clienteLogado
import java.io.IOException

fun adicionarProdutoCarrinho(ctx: Context, produto: Produto) {
    val carrinhoRepository = CarrinhoRepository(
        produto.idProduto,
        clienteLogado.idCliente
    )

    carrinhoRepository.adicionarItemCarrinho(object :
        CarrinhoRepository.Callback {
        override fun onSuccess(codigoRecebido: String) {
            // -1 estoque insuficiente, 0 erro, 1 sucesso
            when (codigoRecebido) {
                "-1" -> Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        ctx,
                        "Estoque insuficiente.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                "0" -> Handler(Looper.getMainLooper()).post {
                    Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                        .show()
                }

                "1" -> Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        ctx,
                        "Item adicionado ao carrinho.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                else -> Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        ctx,
                        "Erro de rede.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        override fun onFailure(e: IOException) {
            // erro de rede
            // não é possível mostrar um Toast de um Thread
            // que não seja UI, então é feito dessa forma
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(ctx, "Erro de rede.", Toast.LENGTH_SHORT)
                    .show()
            }
            Log.e("Erro: ", e.message.toString())
        }
    })
}
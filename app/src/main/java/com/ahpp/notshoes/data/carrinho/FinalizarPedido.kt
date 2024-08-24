package com.ahpp.notshoes.data.carrinho

import com.ahpp.notshoes.api.apiUrl
import com.ahpp.notshoes.model.ItemCarrinho
import com.ahpp.notshoes.model.Venda
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.Executors

class FinalizarPedido(
    private val venda: Venda,
    private val itensList: List<ItemCarrinho>
) {

    interface Callback {
        fun onSuccess(code: String)
        fun onFailure(e: IOException)
    }

    fun sendFinalizarPedido(callback: Callback) {

        val client = OkHttpClient()
        val url = "$apiUrl/cadastrar_venda"

        val json = JsonObject().apply {
            addProperty("dataPedido", venda.dataPedido.toString())
            addProperty("status", venda.status)
            addProperty("detalhesPedido", venda.detalhesPedido)
            addProperty("formaPagamento", venda.formaPagamento)
            addProperty("idCarrinho", venda.idCarrinho)
            add("itensList", convertItensListToJson(itensList))
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        //esse tipo de requisicao precisa ser rodado em um thread
        //por isso o uso do executor

        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val code = response.body?.string()

                if (code != null) {
                    callback.onSuccess(code)
                }
            } catch (e: IOException) {
                callback.onFailure(e)
            } finally {
                executor.shutdown()
            }
        }
    }

    private fun convertItensListToJson(itensList: List<ItemCarrinho>): JsonArray {
        val jsonArray = JsonArray()

        for (item in itensList) {
            val jsonObject = JsonObject().apply {
                addProperty("idProduto", item.idProduto)
                addProperty("quantidade", item.quantidade)
            }
            jsonArray.add(jsonObject)
        }

        return jsonArray
    }
}
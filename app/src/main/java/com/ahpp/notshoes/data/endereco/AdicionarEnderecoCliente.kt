package com.ahpp.notshoes.data.endereco

import com.ahpp.notshoes.api.apiUrl
import com.ahpp.notshoes.constantes.clienteLogado
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.Executors

class AdicionarEnderecoCliente(
    private val estado: String,
    private val cidade: String,
    private val cep: String,
    private val endereco: String,
    private val bairro: String,
    private val numero: String,
    private val complemento: String,
) {

    interface Callback {
        fun onSuccess(code: String)
        fun onFailure(e: IOException)
    }

    fun sendAdicionarEnderecoCliente(callback: Callback) {

        val client = OkHttpClient()
        val url = "$apiUrl/adicionar_endereco_cliente"

        val json = JsonObject().apply {
            addProperty("estado", estado)
            addProperty("cidade", cidade)
            addProperty("cep", cep)
            addProperty("endereco", endereco)
            addProperty("bairro", bairro)
            addProperty("numero", numero)
            addProperty("complemento", complemento)
            addProperty("idCliente", clienteLogado.idCliente)
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
}
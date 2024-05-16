package com.ahpp.notshoes.bd

import com.ahpp.notshoes.model.Cliente
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ClienteRepository {

    private val httpClient = OkHttpClient()
    private lateinit var cliente: Cliente

    fun getCliente(idClienteLogado: Int): Cliente {

        val jsonObj = JsonObject().apply {
            addProperty("idClienteLogado", idClienteLogado)
        }

        val requestBody = jsonObj.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://10.0.2.2:5000/get_cliente")
            .post(requestBody)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        //esse tipo de requisicao precisa ser rodado em um thread
        //por isso o uso do executor
        executor.execute {
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                try {
                    val json = response.body?.string()
                    val gson = Gson()
                    val jsonElement = gson.fromJson(json, JsonElement::class.java)

                    if (jsonElement.isJsonObject) {
                        val clienteJson = jsonElement.asJsonObject
                        cliente = Cliente(
                            //os que tem a verificaçao isJsonNull é porque podem ser nulos
                            idCliente = /*if (clienteJson.has("idCliente"))*/ clienteJson.get("idCliente").asInt /*else ""*/,
                            genero = if (clienteJson.get("genero").isJsonNull) "Não informado" else clienteJson.get("genero").asString,
                            nome = /*if (clienteJson.has("nome"))*/ clienteJson.get("nome").asString /*else ""*/,
                            email = /*if (clienteJson.has("email"))*/ clienteJson.get("email").asString /*else ""*/,
                            senha = /*if (clienteJson.has("senha"))*/ clienteJson.get("senha").asString /*else ""*/,
                            cpf = if (clienteJson.get("cpf").isJsonNull) "Não informado" else clienteJson.get("cpf").asString,
                            idEndereco = if (clienteJson.get("idEndereco").isJsonNull) -1 else clienteJson.get("idEndereco").asInt,
                            idListaDesejos = /*if (clienteJson.has("idListaDesejos"))*/ clienteJson.get("idListaDesejos").asInt /*else ""*/,
                            idCarrinho = /*if (clienteJson.has("idCarrinho"))*/ clienteJson.get("idCarrinho").asInt /*else ""*/
                        )
                    }

                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    executor.shutdown()
                }
            }
        }
        try {
            //espera 5 segundos se nao da timeout
            executor.awaitTermination(5, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return this.cliente
    }
}
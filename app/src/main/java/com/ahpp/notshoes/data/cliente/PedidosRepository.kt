package com.ahpp.notshoes.data.cliente

import android.util.Log
import com.ahpp.notshoes.api.apiUrl
import com.ahpp.notshoes.model.Venda
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

suspend fun getPedidos(idClienteLogado: Int): List<Venda> {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val url = "$apiUrl/get_pedidos_cliente"

        var vendasList: List<Venda> = emptyList()

        val inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)

        val jsonMessage = JsonObject().apply {
            addProperty("idClienteLogado", idClienteLogado)
        }

        val requestBody = jsonMessage.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?.let { json ->
                    val gson = Gson()
                    val jsonElement = gson.fromJson(json, JsonElement::class.java)

                    if (jsonElement.isJsonArray) {
                        val jsonArray = jsonElement.asJsonArray
                        vendasList = jsonArray.map { vendaJson ->
                            val vendaArray = vendaJson.asJsonArray

                            val dataPedido = LocalDate.parse(vendaArray[1].asString, inputFormatter)

                            Venda(
                                vendaArray[0]?.asInt,
                                dataPedido,
                                vendaArray[2].asString,
                                vendaArray[3].asString,
                                vendaArray[4].asString,
                                vendaArray[5].asInt,
                            )
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return@withContext vendasList
    }
}
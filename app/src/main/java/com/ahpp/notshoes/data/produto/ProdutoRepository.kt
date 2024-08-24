package com.ahpp.notshoes.data.produto

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ahpp.notshoes.api.apiUrl
import com.ahpp.notshoes.constantes.clienteLogado
import com.ahpp.notshoes.data.carrinho.CarrinhoRepository
import com.ahpp.notshoes.model.Produto
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
import java.util.concurrent.Executors

class ProdutoRepository {

    private val client = OkHttpClient()

    suspend fun getPromocoes(): List<Produto> {
        return withContext(Dispatchers.IO) {

            val url = "$apiUrl/get_promocoes"

            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { json ->
                        val gson = Gson()
                        val jsonElement = gson.fromJson(json, JsonElement::class.java)

                        if (jsonElement.isJsonArray) {
                            val jsonArray = jsonElement.asJsonArray
                            return@withContext jsonArray.map { produtoJson ->
                                val produtoArray = produtoJson.asJsonArray
                                Produto(
                                    produtoArray[0].asInt,
                                    produtoArray[1].asString,
                                    produtoArray[2].asInt,
                                    produtoArray[3].asString,
                                    produtoArray[4].asString,
                                    produtoArray[5].asString,
                                    produtoArray[6].asString,
                                    produtoArray[7].asString,
                                    produtoArray[8].asString,
                                    produtoArray[9].asBoolean
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    suspend fun getProdutosFiltroIntervalo(intervaloValor: String): List<Produto> {
        return withContext(Dispatchers.IO) {

            val url = "$apiUrl/get_produtos_filtro_intervalo"

            val jsonMessage = JsonObject().apply {
                addProperty("intervaloValor", intervaloValor)
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
                            return@withContext jsonArray.map { produtoJson ->
                                val produtoArray = produtoJson.asJsonArray
                                Produto(
                                    produtoArray[0].asInt,
                                    produtoArray[1].asString,
                                    produtoArray[2].asInt,
                                    produtoArray[3].asString,
                                    produtoArray[4].asString,
                                    produtoArray[5].asString,
                                    produtoArray[6].asString,
                                    produtoArray[7].asString,
                                    produtoArray[8].asString,
                                    produtoArray[9].asBoolean
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    suspend fun filtrarProdutoCategoria(categoria: String): List<Produto> {
        return withContext(Dispatchers.IO) {

            val url = "$apiUrl/filtrar_produto_categoria/$categoria"

            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { json ->
                        val gson = Gson()
                        val jsonElement = gson.fromJson(json, JsonElement::class.java)

                        if (jsonElement.isJsonArray) {
                            val jsonArray = jsonElement.asJsonArray
                            return@withContext jsonArray.map { produtoJson ->
                                val produtoArray = produtoJson.asJsonArray
                                Produto(
                                    produtoArray[0].asInt,
                                    produtoArray[1].asString,
                                    produtoArray[2].asInt,
                                    produtoArray[3].asString,
                                    produtoArray[4].asString,
                                    produtoArray[5].asString,
                                    produtoArray[6].asString,
                                    produtoArray[7].asString,
                                    produtoArray[8].asString,
                                    produtoArray[9].asBoolean
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    suspend fun buscarProdutoNome(nome: String): List<Produto> {
        return withContext(Dispatchers.IO) {

            val url = "$apiUrl/busca_produto/$nome"

            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { json ->
                        val gson = Gson()
                        val jsonElement = gson.fromJson(json, JsonElement::class.java)

                        if (jsonElement.isJsonArray) {
                            val jsonArray = jsonElement.asJsonArray
                            return@withContext jsonArray.map { produtoJson ->
                                val produtoArray = produtoJson.asJsonArray
                                Produto(
                                    produtoArray[0].asInt,
                                    produtoArray[1].asString,
                                    produtoArray[2].asInt,
                                    produtoArray[3].asString,
                                    produtoArray[4].asString,
                                    produtoArray[5].asString,
                                    produtoArray[6].asString,
                                    produtoArray[7].asString,
                                    produtoArray[8].asString,
                                    produtoArray[9].asBoolean
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    suspend fun getProdutosListaDesejos(idListaDesejos: Int): List<Produto> {
        return withContext(Dispatchers.IO) {
            val url = "$apiUrl/get_lista_desejos"

            val jsonMessage = JsonObject().apply {
                addProperty("idListaDesejos", idListaDesejos)
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
                            return@withContext jsonArray.map { produtoJson ->
                                val produtoArray = produtoJson.asJsonArray
                                Produto(
                                    produtoArray[0].asInt,
                                    produtoArray[1].asString,
                                    produtoArray[2].asInt,
                                    produtoArray[3].asString,
                                    produtoArray[4].asString,
                                    produtoArray[5].asString,
                                    produtoArray[6].asString,
                                    produtoArray[7].asString,
                                    produtoArray[8].asString,
                                    produtoArray[9].asBoolean
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    fun removerProdutoListaDesejos(idProduto: Int, idCliente: Int) {

        val url = "$apiUrl/remover_lista_desejos"

        val jsonMessage = JsonObject().apply {
            addProperty("idProduto", idProduto)
            addProperty("idCliente", idCliente)
        }

        val requestBody = jsonMessage.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val code = response.body?.string().toString()
                Log.e("Codigo recebido (removerProdutoListaDesejos): ", code)
            } catch (e: IOException) {
                Log.e("Erro (removerProdutoListaDesejos)", "Erro de rede.", e)
            } finally {
                executor.shutdown()
            }
        }
    }

    fun adicionarProdutoListaDesejos(idProduto: Int, idCliente: Int) {

        val url = "$apiUrl/adicionar_lista_desejos"

        val jsonMessage = JsonObject().apply {
            addProperty("idProduto", idProduto)
            addProperty("idCliente", idCliente)
        }

        val requestBody = jsonMessage.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val code = response.body?.string().toString()
                Log.e("Codigo recebido (adicionarProdutoListaDesejos): ", code)
            } catch (e: IOException) {
                Log.e("Erro (adicionarProdutoListaDesejos)", "Erro de rede.", e)
            } finally {
                executor.shutdown()
            }
        }
    }

    fun verificarProdutoListaDesejos(
        idProduto: Int,
        idListaDesejos: Int,
        callback: (String) -> Unit
    ) {

        val url = "$apiUrl/verificar_produto_lista_desejos"

        val jsonMessage = JsonObject().apply {
            addProperty("idProduto", idProduto)
            addProperty("idListaDesejos", idListaDesejos)
        }

        val requestBody = jsonMessage.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val response = client.newCall(request).execute()
                val resultado = response.body?.string().toString()
                callback(resultado)
            } catch (e: IOException) {
                Log.e("Erro (verificarProdutoListaDesejos) ", "Erro de rede.", e)
            } finally {
                executor.shutdown()
            }
        }
    }
}

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
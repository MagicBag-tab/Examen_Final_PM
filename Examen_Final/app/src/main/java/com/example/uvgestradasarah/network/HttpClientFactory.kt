package com.example.uvgestradasarah.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            defaultRequest {
                url("https://api.coincap.io/v2/")
                header("Authorization", "Bearer 6f8c2f757cc81e9950a05aeed8292abff853114ebc731977f3f5a580b1e9371a")
            }
        }
    }
}
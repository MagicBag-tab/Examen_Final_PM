package com.example.uvgestradasarah.network.api

import com.example.uvgestradasarah.network.assetDto.AssetResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AssetsApi(
    private val httpClient: HttpClient
){
    suspend fun getAssets(): Result<AssetResponse> {
        return try {
            val response = httpClient.get("character")
            val characters: AssetResponse = response.body()
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
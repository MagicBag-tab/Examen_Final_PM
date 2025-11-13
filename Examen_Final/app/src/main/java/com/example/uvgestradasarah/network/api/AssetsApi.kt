package com.example.uvgestradasarah.network.api

import com.example.uvgestradasarah.network.assetDto.AssetDto
import com.example.uvgestradasarah.network.assetDto.AssetResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

class AssetsApi(
    private val httpClient: HttpClient
) {
    suspend fun getAssets(): Result<AssetResponse> {
        return try {
            val response = httpClient.get("assets")
            val assets: AssetResponse = response.body()
            Result.success(assets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAssetById(id: String): Result<AssetDetailResponse> {
        return try {
            val response = httpClient.get("assets/$id")
            val asset: AssetDetailResponse = response.body()
            Result.success(asset)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Serializable
data class AssetDetailResponse(
    val data: AssetDto
)
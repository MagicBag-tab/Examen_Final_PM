package com.example.uvgestradasarah.network.assetDto

import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    val data: List<AssetDto>
)

@Serializable
data class AssetDto(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String
)
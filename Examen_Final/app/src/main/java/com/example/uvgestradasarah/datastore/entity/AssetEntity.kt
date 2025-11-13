package com.example.uvgestradasarah.datastore.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uvgestradasarah.Assets

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String
) {
    fun toAssets(): Assets {
        return Assets(
            id = id,
            name = name,
            symbol = symbol,
            priceUsd = priceUsd,
            changePercent24Hr = changePercent24Hr,
            supply = supply,
            maxSupply = maxSupply,
            marketCapUsd = marketCapUsd
        )
    }
}

fun Assets.toEntity(): AssetEntity {
    return AssetEntity(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd
    )
}
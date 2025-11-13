package com.example.uvgestradasarah.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.uvgestradasarah.Assets
import com.example.uvgestradasarah.datastore.AssetDatabase
import com.example.uvgestradasarah.datastore.entity.toEntity
import com.example.uvgestradasarah.network.HttpClientFactory
import com.example.uvgestradasarah.network.api.AssetsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "assets_prefs")

class AssetsRepository(private val context: Context) {
    private val client = HttpClientFactory.create()
    private val api = AssetsApi(client)
    private val database = AssetDatabase.getDatabase(context)
    private val assetDao = database.assetDao()

    companion object {
        private val LAST_SAVED_KEY = stringPreferencesKey("last_saved_date")
    }

    suspend fun getAssets(): Result<List<Assets>> {
        return try {
            val result = api.getAssets()
            if (result.isSuccess) {
                val assets = result.getOrNull()?.data?.map { dto ->
                    Assets(
                        id = dto.id,
                        name = dto.name,
                        symbol = dto.symbol,
                        priceUsd = dto.priceUsd,
                        changePercent24Hr = dto.changePercent24Hr,
                        supply = dto.supply,
                        maxSupply = dto.maxSupply ?: "N/A",
                        marketCapUsd = dto.marketCapUsd
                    )
                } ?: emptyList()
                Result.success(assets)
            } else {
                val localAssets = getLocalAssets()
                if (localAssets.isNotEmpty()) {
                    Result.success(localAssets)
                } else {
                    Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
                }
            }
        } catch (e: Exception) {
            val localAssets = getLocalAssets()
            if (localAssets.isNotEmpty()) {
                Result.success(localAssets)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getAssetById(id: String): Result<Assets> {
        return try {
            val result = api.getAssetById(id)
            if (result.isSuccess) {
                val dto = result.getOrNull()?.data
                if (dto != null) {
                    val asset = Assets(
                        id = dto.id,
                        name = dto.name,
                        symbol = dto.symbol,
                        priceUsd = dto.priceUsd,
                        changePercent24Hr = dto.changePercent24Hr,
                        supply = dto.supply,
                        maxSupply = dto.maxSupply ?: "N/A",
                        marketCapUsd = dto.marketCapUsd
                    )
                    Result.success(asset)
                } else {
                    val localAsset = getLocalAssetById(id)
                    if (localAsset != null) {
                        Result.success(localAsset)
                    } else {
                        Result.failure(Exception("Asset no encontrado"))
                    }
                }
            } else {
                val localAsset = getLocalAssetById(id)
                if (localAsset != null) {
                    Result.success(localAsset)
                } else {
                    Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
                }
            }
        } catch (e: Exception) {
            val localAsset = getLocalAssetById(id)
            if (localAsset != null) {
                Result.success(localAsset)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun saveAssetsLocally(assets: List<Assets>) {
        try {
            val entities = assets.map { it.toEntity() }
            assetDao.insertAssets(entities)

            val currentDate: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            context.dataStore.edit { prefs ->
                prefs[LAST_SAVED_KEY] = currentDate
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getLocalAssets(): List<Assets> {
        return try {
            assetDao.getAllAssets().map { it.toAssets() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getLocalAssetById(id: String): Assets? {
        return try {
            assetDao.getAssetById(id)?.toAssets()
        } catch (e: Exception) {
            null
        }
    }

    fun getLastSavedDate(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[LAST_SAVED_KEY]
        }
    }

    suspend fun hasLocalData(): Boolean {
        return try {
            assetDao.getAssetsCount() > 0
        } catch (e: Exception) {
            false
        }
    }
}


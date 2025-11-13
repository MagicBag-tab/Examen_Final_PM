package com.example.uvgestradasarah.datastore.assetDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uvgestradasarah.datastore.entity.AssetEntity

@Dao
interface AssetDao {

    @Query("SELECT * FROM assets")
    suspend fun getAllAssets(): List<AssetEntity>

    @Query("SELECT * FROM assets WHERE id = :assetId")
    suspend fun getAssetById(assetId: String): AssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetEntity)

    @Query("DELETE FROM assets")
    suspend fun deleteAllAssets()

    @Query("SELECT COUNT(*) FROM assets")
    suspend fun getAssetsCount(): Int
}
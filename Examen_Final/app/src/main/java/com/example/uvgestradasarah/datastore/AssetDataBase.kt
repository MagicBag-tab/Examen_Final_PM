package com.example.uvgestradasarah.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uvgestradasarah.datastore.assetDao.AssetDao
import com.example.uvgestradasarah.datastore.entity.AssetEntity

@Database(
    entities = [AssetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AssetDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao

    companion object {
        @Volatile
        private var INSTANCE: AssetDatabase? = null

        fun getDatabase(context: Context): AssetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AssetDatabase::class.java,
                    "asset_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
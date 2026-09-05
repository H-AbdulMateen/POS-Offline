package com.abdulmateen.pos_offline.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.abdulmateen.pos_offline.core.domain.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>
): DataStoreManager {
    override suspend fun setBoolValue(key: String, value: Boolean) {
        withContext(Dispatchers.IO){
            dataStore
                .edit {
                    it[booleanPreferencesKey(key)] = value
                }
        }
    }

    override suspend fun getBoolValue(key: String): Boolean {
        return withContext(Dispatchers.IO){
            dataStore
                .data
                .map {
                    it[booleanPreferencesKey(key)] ?: false
                }
        }.first()
    }

    override suspend fun setStringValue(key: String, value: String) {
        withContext(Dispatchers.IO){
            dataStore
                .edit {
                    it[stringPreferencesKey(key)] = value
                }
        }
    }

    override suspend fun getStringValue(key: String): String {
        return withContext(Dispatchers.IO){
            dataStore
                .data
                .map {
                    it[stringPreferencesKey(key)] ?: ""
                }
        }.first()
    }

    override suspend fun setIntValue(key: String, value: Int) {
        withContext(Dispatchers.IO){
            dataStore
                .edit {
                    it[intPreferencesKey(key)] = value
                }
        }
    }

    override suspend fun setDoubleValue(key: String, value: Double) {
        withContext(Dispatchers.IO){
            dataStore.edit {
                it[doublePreferencesKey(key)] = value
            }
        }
    }

    override suspend fun getDoubleValue(key: String): Double {
        return withContext(Dispatchers.IO){
            dataStore
                .data
                .map {
                    it[doublePreferencesKey(key)] ?: 0.0
                }
        }.first()
    }

    override suspend fun getIntValue(key: String): Int {
        return withContext(Dispatchers.IO){
            dataStore
                .data
                .map {
                    it[intPreferencesKey(key)] ?: 0
                }
        }.first()
    }
}
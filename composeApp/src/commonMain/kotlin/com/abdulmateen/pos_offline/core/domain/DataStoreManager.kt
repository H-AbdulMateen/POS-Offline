package com.abdulmateen.pos_offline.core.domain

interface DataStoreManager {
    suspend fun setBoolValue(key: String, value: Boolean)
    suspend fun getBoolValue(key: String): Boolean
    suspend fun setStringValue(key: String, value: String)
    suspend fun getStringValue(key: String): String
    suspend fun setIntValue(key: String, value: Int)
    suspend fun getIntValue(key: String): Int
}
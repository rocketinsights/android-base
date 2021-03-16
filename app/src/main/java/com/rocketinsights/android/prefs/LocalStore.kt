package com.rocketinsights.android.prefs

import kotlinx.coroutines.flow.Flow

interface LocalStore {

    fun getStringValue(key: String): Flow<String>

    suspend fun setStringValue(key: String, value: String)

    fun getBooleanValue(key: String): Flow<Boolean>

    suspend fun setBooleanValue(key: String, value: Boolean)

    fun getIntValue(key: String): Flow<Int>

    suspend fun setIntValue(key: String, value: Int)

    fun getLongValue(key: String): Flow<Long>

    suspend fun setLongValue(key: String, value: Long)

    fun getFloatValue(key: String): Flow<Float>

    suspend fun setFloatValue(key: String, value: Float)

    fun getDoubleValue(key: String): Flow<Double>

    suspend fun setDoubleValue(key: String, value: Double)

    fun getStringSetValue(key: String): Flow<Set<String>>

    suspend fun setStringSetValue(key: String, value: Set<String>)
}
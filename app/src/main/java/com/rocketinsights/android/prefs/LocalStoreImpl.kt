package com.rocketinsights.android.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCE_FILE_NAME = "com.rocketinsights.android.USER_PREFS"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_FILE_NAME)

class LocalStoreImpl(context: Context) : LocalStore {

    private val dataStore = context.dataStore

    override fun getStringValue(key: String): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: ""
        }
    }

    override suspend fun setStringValue(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getBooleanValue(key: String): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: false
        }
    }

    override suspend fun setBooleanValue(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getIntValue(key: String): Flow<Int> {
        val prefKey = intPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: 0
        }
    }

    override suspend fun setIntValue(key: String, value: Int) {
        val prefKey = intPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getLongValue(key: String): Flow<Long> {
        val prefKey = longPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: 0L
        }
    }

    override suspend fun setLongValue(key: String, value: Long) {
        val prefKey = longPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getFloatValue(key: String): Flow<Float> {
        val prefKey = floatPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: 0.0F
        }
    }

    override suspend fun setFloatValue(key: String, value: Float) {
        val prefKey = floatPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getDoubleValue(key: String): Flow<Double> {
        val prefKey = doublePreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: 0.0
        }
    }

    override suspend fun setDoubleValue(key: String, value: Double) {
        val prefKey = doublePreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }

    override fun getStringSetValue(key: String): Flow<Set<String>> {
        val prefKey = stringSetPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[prefKey] ?: emptySet()
        }
    }

    override suspend fun setStringSetValue(key: String, value: Set<String>) {
        val prefKey = stringSetPreferencesKey(key)
        dataStore.edit { prefs ->
            prefs[prefKey] = value
        }
    }
}
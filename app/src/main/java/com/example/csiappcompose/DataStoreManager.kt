package com.example.csiappcompose

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

object DataStoreManager {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val COMPLETE_DETAILS_STATUS = booleanPreferencesKey("complete_details_status") // Changed to boolean

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    fun getToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    suspend fun saveCompleteDetailsStatus(context: Context, isComplete: Boolean) {
        Log.i("DATA MANAGER", "saving status= $isComplete")
        context.dataStore.edit { prefs ->
            prefs[COMPLETE_DETAILS_STATUS] = isComplete
        }
    }

    fun getCompleteDetailsStatus(context: Context): Flow<Boolean?> {
        return context.dataStore.data.map { prefs ->
            prefs[COMPLETE_DETAILS_STATUS]
        }.catch { emit(null) }
    }
}
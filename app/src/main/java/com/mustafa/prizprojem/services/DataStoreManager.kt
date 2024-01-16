package com.mustafa.prizprojem.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "Tokens")
    private val dataStore = context.dataStore


    companion object{
        val myToken = stringPreferencesKey("USER_TOKEN")

    }

    suspend fun setToken(token:String){
        dataStore.edit {
            it[myToken] = token
        }
    }

    fun getToken() : Flow<String>{
        return dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }
                else{
                    throw exception
                }

            }
            .map { pref->
                 pref[myToken] ?: ""

            }
    }


}
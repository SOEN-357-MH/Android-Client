package com.example.moviehub.helpers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.moviehub.misc.Constants
import com.example.moviehub.models.UserBody
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKey {
        val userInfo = stringPreferencesKey(Constants.USER_INFO)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_FILE_NAME)

    /*** User Info ***/
    suspend fun saveUserInfo(userBody: UserBody?){
        context.dataStore.edit { preferences ->
            val jsonString = GsonBuilder().create().toJson(userBody)
            preferences[PreferencesKey.userInfo] = jsonString
        }
    }

    val readUserInfo: Flow<UserBody?> = context.dataStore.data
        .catch { e ->
            if(e is IOException){
                Timber.d(e.message.toString())
                emit(emptyPreferences())
            }else {
                throw e
            }
        }
        .map { preferences ->
            val value = preferences[PreferencesKey.userInfo]
            GsonBuilder().create().fromJson(value, UserBody::class.java)
        }

    /***Clear Data Store***/
    suspend fun clearDataStore(){
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
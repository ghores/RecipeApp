package com.example.recipeapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.recipeapp.data.source.RemoteDataSource
import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.models.register.RegisterStoredModel
import com.example.recipeapp.models.register.ResponseRegister
import com.example.recipeapp.utils.Constants.REGISTER_HASH
import com.example.recipeapp.utils.Constants.REGISTER_USERNAME
import com.example.recipeapp.utils.Constants.REGISTER_USERNAME_INFO
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteDataSource
) {

    //Store user info
    private object StoredKeys {
        val username = stringPreferencesKey(REGISTER_USERNAME)
        val hash = stringPreferencesKey(REGISTER_HASH)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = REGISTER_USERNAME_INFO)

    suspend fun saveRegisterData(username: String, hash: String) {
        context.dataStore.edit {
            it[StoredKeys.username] = username
            it[StoredKeys.hash] = hash
        }
    }

    val readRegisterData: Flow<RegisterStoredModel> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map {
            val username = it[StoredKeys.username] ?: ""
            val hash = it[StoredKeys.hash] ?: ""
            RegisterStoredModel(username, hash)
        }

    //Api
    suspend fun postRegister(apikey: String, body: BodyRegister): Response<ResponseRegister> =
        remoteDataSource.postRegister(apikey, body)
}
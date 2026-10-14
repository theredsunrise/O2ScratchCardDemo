package com.example.scratchcardo2demo.data.persistence

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.scratchcardo2demo.data.persistence.mapper.toDomain
import com.example.scratchcardo2demo.data.persistence.mapper.toDto
import com.example.scratchcardo2demo.data.persistence.model.ScratchCardDto
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File

class DataStoreScratchCardPersistenceRepository(
    private val json: Json,
    private val context: Context,
    private val preferencesName: String,
    scope: CoroutineScope
) : ScratchCardPersistenceRepository {

    private val key = stringPreferencesKey("scratch_card_data")
    private val dataStore: DataStore<Preferences>

    private val _state = MutableSharedFlow<ScratchCard?>(replay = 1)
    override val state: Flow<ScratchCard?> = _state

    init {
        dataStore = PreferenceDataStoreFactory.create {
            check(preferencesName.isNotBlank()) { "App preferences name cannot be empty or blank." }
            File(ContextCompat.getNoBackupFilesDir(context),
                "datastore/${preferencesName}.preferences_pb")
        }
        scope.launch {
            _state.emit(get())
        }
    }

    override suspend fun getOrCreate(card: ScratchCard): ScratchCard =
        get() ?: run {
            dataStore.edit {
                val dto = card.toDto()
                it[key] = json.encodeToString(dto)
                _state.emit(card)
            }
            card
        }

    override suspend fun get(): ScratchCard? {
        val preferences = dataStore.data.first()
        val cardData = preferences.get(key = key) ?: return null
        val dto = json.decodeFromString<ScratchCardDto>(cardData)
        return dto.toDomain()
    }

    override suspend fun update(card: ScratchCard) {
        dataStore.edit {
            val dto = card.toDto()
            it[key] = json.encodeToString(dto)
            _state.emit(card)
        }
    }
}
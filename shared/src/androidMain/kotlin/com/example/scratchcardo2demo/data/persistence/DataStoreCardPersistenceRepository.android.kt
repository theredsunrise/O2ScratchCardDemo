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
import com.example.scratchcardo2demo.domain.ScratchCard
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import java.io.File

class DataStoreScratchCardPersistenceRepository(
    val json: Json,
    val context: Context
) : ScratchCardPersistenceRepository {

    private val key = stringPreferencesKey("scratch_card_data")

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        File(ContextCompat.getNoBackupFilesDir(context), "datastore/app_preferences")
    }

    override suspend fun get(): ScratchCard? {
        val preferences =
            requireNotNull(dataStore.data.firstOrNull()) { "App preferences does not exist." }
        val cardData = preferences.get(key = key) ?: return null
        val dto = json.decodeFromString<ScratchCardDto>(cardData)
        return dto.toDomain()
    }

    override suspend fun update(card: ScratchCard) {
        val dto = card.toDto()
        dataStore.edit {
            it[key] = json.encodeToString(dto)
        }
    }
}
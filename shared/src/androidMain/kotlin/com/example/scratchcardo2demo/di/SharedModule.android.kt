package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.data.persistence.DataStoreScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformSharedModule = module {
    single<ScratchCardPersistenceRepository> {
        DataStoreScratchCardPersistenceRepository(
            json = get(),
            context = androidContext(),
            preferencesName = Config.Persistence.PREFERENCES_NAME,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
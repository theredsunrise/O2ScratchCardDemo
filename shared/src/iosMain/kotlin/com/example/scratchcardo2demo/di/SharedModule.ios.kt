package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.config.Config
import com.example.scratchcardo2demo.data.persistence.FileScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

actual val platformSharedModule = module {
    single<ScratchCardPersistenceRepository> {
        FileScratchCardPersistenceRepository(
            json = get(),
            preferencesName = Config.Persistence.PREFERENCES_NAME,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}

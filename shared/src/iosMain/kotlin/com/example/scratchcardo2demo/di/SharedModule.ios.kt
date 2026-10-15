package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.config.Config
import com.example.scratchcardo2demo.data.persistence.FileScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import org.koin.dsl.module

actual val platformSharedModule = module {
    single<ScratchCardPersistenceRepository> {
        FileScratchCardPersistenceRepository(
            json = get(),
            preferencesName = Config.Persistence.PREFERENCES_NAME,
            scope = get()
        )
    }
}

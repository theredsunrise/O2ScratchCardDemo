package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.data.persistence.DataStoreScratchCardPersistenceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformSharedModule = module {
    single {
        DataStoreScratchCardPersistenceRepository(
            json = get(),
            context = androidContext()
        )
    }
}
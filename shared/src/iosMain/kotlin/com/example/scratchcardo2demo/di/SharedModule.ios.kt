package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.data.persistence.FileScratchCardPersistenceRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformSharedModule = module {
    singleOf(::FileScratchCardPersistenceRepository)
}
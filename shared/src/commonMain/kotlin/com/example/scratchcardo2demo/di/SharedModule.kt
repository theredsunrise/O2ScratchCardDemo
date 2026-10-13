package com.example.scratchcardo2demo.di

import androidx.lifecycle.SavedStateHandle
import com.example.scratchcardo2demo.presentation.viewmodel.ScratchCardScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformSharedModule: Module

val sharedModule = module {
    includes(platformSharedModule)
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    viewModel {
        ScratchCardScreenViewModel(
            dispatcher = Dispatchers.IO,
            savedState = get<SavedStateHandle>()
        )
    }
}

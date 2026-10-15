package com.example.scratchcardo2demo.di

import com.example.scratchcardo2demo.config.Config
import com.example.scratchcardo2demo.data.networking.HttpClientFactory
import com.example.scratchcardo2demo.data.networking.KtorCardNetworkRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardNetworkRepository
import com.example.scratchcardo2demo.domain.manager.DefaultScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.usecase.ActivateCardUseCase
import com.example.scratchcardo2demo.domain.usecase.DefaultActivateCardUseCase
import com.example.scratchcardo2demo.domain.usecase.DefaultScratchCardUseCase
import com.example.scratchcardo2demo.domain.usecase.ScratchCardUseCase
import com.example.scratchcardo2demo.presentation.viewmodel.ActivateCardScreenViewModel
import com.example.scratchcardo2demo.presentation.viewmodel.AppViewModel
import com.example.scratchcardo2demo.presentation.viewmodel.ScratchCardScreenViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformSharedModule: Module

val sharedModule = module {
    includes(platformSharedModule)
    viewModel {
        AppViewModel(
            dispatcher = Dispatchers.IO,
            persistenceRepository = get()
        )
    }
    viewModel {
        ScratchCardScreenViewModel(
            dispatcher = Dispatchers.IO,
            scratchCardUseCase = get(),
            persistenceRepository = get()
        )
    }
    viewModel {
        ActivateCardScreenViewModel(
            dispatcher = Dispatchers.IO,
            activatorManager = get(),
            persistenceRepository = get()
        )
    }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single<HttpClient> { HttpClientFactory.createDefault() }
    single<ScratchCardNetworkRepository> {
        KtorCardNetworkRepository(
            endpoints = KtorCardNetworkRepository.Endpoints(
                baseUrl = Config.Networking.BASE_URL,
                versionEndpoint = Config.Networking.VERSION_ENDPOINT
            ),
            httpClient = get()
        )
    }
    factoryOf(::DefaultScratchCardUseCase) bind ScratchCardUseCase::class
    factoryOf(::DefaultActivateCardUseCase) bind ActivateCardUseCase::class
    single<CoroutineScope> { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single<ScratchCardActivatorManager> { DefaultScratchCardActivatorManager(get(), get()) }
}

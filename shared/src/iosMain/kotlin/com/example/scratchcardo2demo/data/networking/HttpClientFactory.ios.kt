package com.example.scratchcardo2demo.data.networking

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual val engineProvider: HttpClientEngine
    get() = Darwin.create()
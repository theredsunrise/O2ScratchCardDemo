package com.example.scratchcardo2demo.data.networking

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual val engineProvider: HttpClientEngine
    get() = Android.create()
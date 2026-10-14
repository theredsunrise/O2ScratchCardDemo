package com.example.scratchcardo2demo.di

import android.content.Context
import org.koin.android.ext.koin.androidContext

fun androidInitKoin(applicationContext: Context) {
    initKoin {
        androidContext(applicationContext)
    }
}
package com.example.scratchcardo2demo

import android.app.Application
import com.example.scratchcardo2demo.di.androidInitKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        androidInitKoin(applicationContext = this)
    }
}
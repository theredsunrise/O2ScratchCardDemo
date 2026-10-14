package com.example.scratchcardo2demo.shared

actual fun isMainThread(): Boolean = Thread.currentThread().name == "main"
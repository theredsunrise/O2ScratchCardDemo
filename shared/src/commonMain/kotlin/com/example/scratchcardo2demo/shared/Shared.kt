package com.example.scratchcardo2demo.shared

expect fun isMainThread(): Boolean

fun assertIsMainThread() = check (isMainThread())
fun assertIsNotMainThread() = check (!isMainThread())
package com.example.scratchcardo2demo.shared

import platform.Foundation.NSThread

actual fun isMainThread(): Boolean = NSThread.isMainThread
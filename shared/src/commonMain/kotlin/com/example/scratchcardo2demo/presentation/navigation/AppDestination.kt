package com.example.scratchcardo2demo.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

sealed interface AppDestination : NavKey {
    @Serializable
    data object ScratchCard : AppDestination
    companion object {
        val SAVED_STATE_CONFIG = SavedStateConfiguration {
            this.serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(ScratchCard::class, ScratchCard.serializer())
                }
            }
        }
    }
}
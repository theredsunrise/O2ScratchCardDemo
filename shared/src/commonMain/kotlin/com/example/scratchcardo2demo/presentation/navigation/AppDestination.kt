package com.example.scratchcardo2demo.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface AppDestination : NavKey {
    @Serializable
    data object ScratchCard : AppDestination

    @Serializable
    data object ActivateCard : AppDestination

    @Serializable
    data object AppScreen : AppDestination

    companion object {
        val SAVED_STATE_CONFIG = SavedStateConfiguration {
            this.serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(AppScreen::class, AppScreen.serializer())
                    subclass(ScratchCard::class, ScratchCard.serializer())
                    subclass(ActivateCard::class, ActivateCard.serializer())
                }
            }
        }
    }
}
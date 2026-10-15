package com.example.scratchcardo2demo.data.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionDto(

    @SerialName("android")
    val android: String)
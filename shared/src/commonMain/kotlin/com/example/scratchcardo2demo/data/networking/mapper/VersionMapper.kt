package com.example.scratchcardo2demo.data.networking.mapper

import com.example.scratchcardo2demo.data.networking.model.Version
import com.example.scratchcardo2demo.data.networking.model.VersionDto

fun VersionDto.toDomain(): Version =
    Version(android = this.android.toInt())

fun Version.toDto(): VersionDto =
    VersionDto(android = this.android.toString())

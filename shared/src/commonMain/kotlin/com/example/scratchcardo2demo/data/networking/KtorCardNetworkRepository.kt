package com.example.scratchcardo2demo.data.networking

import com.example.scratchcardo2demo.data.networking.mapper.toDomain
import com.example.scratchcardo2demo.data.networking.model.Version
import com.example.scratchcardo2demo.data.networking.model.VersionDto
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardNetworkRepository
import com.example.scratchcardo2demo.ext.get
import io.ktor.client.HttpClient
import kotlin.uuid.Uuid

class KtorCardNetworkRepository(
    private val endpoints: Endpoints,
    private val httpClient: HttpClient
) : ScratchCardNetworkRepository {
    data class Endpoints(val baseUrl: String, val versionEndpoint: String)

    override suspend fun fetchVersion(cardUuid: Uuid): Version =
        httpClient.get<VersionDto>(
            baseUrl = endpoints.baseUrl, endpoint = endpoints.versionEndpoint,
            params = mapOf("code" to cardUuid.toString())
        ).toDomain()
}
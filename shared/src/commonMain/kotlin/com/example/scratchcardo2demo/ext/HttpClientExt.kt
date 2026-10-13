package com.example.scratchcardo2demo.ext

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters

suspend inline fun <reified O> HttpClient.get(
    baseUrl: String,
    endpoint: String,
    params: Map<String, String>
): O =
    this.get(urlString = baseUrl) {
        url {
            appendPathSegments(endpoint)
            parameters {
                params.forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
    }.body<O>()

suspend inline fun <reified I, reified O> HttpClient.post(
    baseUrl: String,
    endpoint: String,
    body: I
): O =
    this.post(urlString = baseUrl) {
        this.accept(ContentType.Application.Json)
        this.url {
            appendPathSegments(endpoint)
        }
        setBody(body = body)
    }.body<O>()

package com.example.scratchcardo2demo.domain.usecase

import kotlin.uuid.Uuid

sealed class ActivateCardUseCaseException: Exception() {
    class CardNotFoundException: ActivateCardUseCaseException()
    data class InsufficientVersionForActivationException(val version: Int) :
        ActivateCardUseCaseException()
}
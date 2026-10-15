package com.example.scratchcardo2demo.presentation.mapper

import com.example.scratchcardo2demo.domain.model.ScratchCardException
import com.example.scratchcardo2demo.domain.usecase.ActivateCardUseCaseException
import org.jetbrains.compose.resources.StringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_card_not_found
import scratchcardo2demo.shared.generated.resources.error_insufficient_version
import scratchcardo2demo.shared.generated.resources.error_invalid_state_transition
import scratchcardo2demo.shared.generated.resources.error_unknown

sealed interface PresentationError {
    data class Resource(val value: StringResource) : PresentationError
    data class Text(val value: String) : PresentationError
}

fun Throwable.toPresentationError(): PresentationError = when (this) {
    is ActivateCardUseCaseException.CardNotFoundException ->
        PresentationError.Resource(Res.string.error_card_not_found)

    is ActivateCardUseCaseException.InsufficientVersionForActivationException ->
        PresentationError.Resource(Res.string.error_insufficient_version)

    is ScratchCardException.InvalidStateTransitionException ->
        PresentationError.Resource(Res.string.error_invalid_state_transition)

    else -> message
        ?.takeIf(String::isNotBlank)
        ?.let(PresentationError::Text)
        ?: cause?.message
            ?.takeIf(String::isNotBlank)
            ?.let(PresentationError::Text)
        ?: PresentationError.Resource(Res.string.error_unknown)
}

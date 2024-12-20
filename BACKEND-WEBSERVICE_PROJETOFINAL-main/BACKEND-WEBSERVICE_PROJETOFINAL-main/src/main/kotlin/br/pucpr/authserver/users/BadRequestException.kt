package br.pucpr.authserver.users

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

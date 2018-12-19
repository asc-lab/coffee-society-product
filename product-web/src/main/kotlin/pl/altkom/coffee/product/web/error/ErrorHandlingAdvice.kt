package pl.altkom.coffee.product.web.error

import mu.KLogging
import org.axonframework.messaging.RemoteHandlingException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.concurrent.CompletionException

@RestControllerAdvice
class ErrorHandlingAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: CompletionException): String {
        logger.error("Error while handling command", exception.cause)

        if (exception.cause is RemoteHandlingException) {
            return (exception.cause as RemoteHandlingException).exceptionDescriptions.joinToString()
        }

        return exception.toString()
    }

    companion object : KLogging()
}

package com.eddie.credit.application.system.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class, BusinessException::class, IllegalArgumentException::class)
    fun handleBadRequestExceptions(ex: Exception): ResponseEntity<ExceptionDetails> {
        val status = HttpStatus.BAD_REQUEST
        return buildResponseEntity(
            status,
            "Bad Request! Consult the documentation",
            ex,
            mutableMapOf(ex.cause.toString() to ex.message)
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        val status = HttpStatus.CONFLICT
        return buildResponseEntity(
            status,
            "Conflict! Consult the documentation",
            ex,
            mutableMapOf(ex.cause.toString() to ex.message)
        )
    }

    private fun buildResponseEntity(
        status: HttpStatus,
        title: String,
        ex: Exception,
        details: MutableMap<String, String?>
    ): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(status)
            .body(
                ExceptionDetails(
                    title = title,
                    timestamp = LocalDateTime.now(),
                    status = status.value(),
                    exception = ex.javaClass.toString(),
                    details = details
                )
            )
    }
}

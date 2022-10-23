package pt.isel.daw.tictactow.http.pipeline

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.isel.daw.tictactow.http.model.Problem

@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleBindException(
        ex: BindException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.info("Handling BindException: {}", ex.message)
        return Problem.response(400, Problem.invalidRequestContent)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        log.info("Handling HttpMessageNotReadableException: {}", ex.message)
        return Problem.response(400, Problem.invalidRequestContent)
    }

    @ExceptionHandler(
        Exception::class,
    )
    fun handleAll(): ResponseEntity<Unit> {
        return ResponseEntity.status(500).build()
    }

    companion object {
        private val log = LoggerFactory.getLogger(CustomExceptionHandler::class.java)
    }
}
package br.com.zup.keymanager.shared

import io.grpc.Status
import io.grpc.Status.*
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 404 quando statusException for not found`(){

        val mensagem = "não encontrado"

        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)


    }

    @Test
    fun `deve retornar 422 quando statusException for already existis`(){

        val mensagem = "chave pix já existente"

        val alreadyExistsException = StatusRuntimeException(ALREADY_EXISTS.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

    @Test
    fun `deve retornar 400 quando statusException for invalid argument`(){
        val mensagem = "Dados da requisição estão inválidos"

        val invalidArgumentException = StatusRuntimeException(INVALID_ARGUMENT.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 500 quando qualquer outro erro for lancado`(){
        val mensagem = "Não foi possível completar a requisição"

        val internalServerErrorException = StatusRuntimeException(ABORTED.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, internalServerErrorException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }
}
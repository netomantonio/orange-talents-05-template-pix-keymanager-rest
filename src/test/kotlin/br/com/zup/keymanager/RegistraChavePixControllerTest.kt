package br.com.zup.keymanager

import br.com.zup.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.RegistraChaveResponse
import br.com.zup.keymanager.TipoDeChaveRequest.*
import br.com.zup.keymanager.TipoDeContaRequest.*
import br.com.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest {

    @field:Inject
    lateinit var registraStub: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve registrar uma nova chave pix`(){

        // cenário
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChaveResponse.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.registrar(Mockito.any())).willReturn(respostaGrpc)

        val novaChavePix = NovaChaveRequest(
            tipoDeConta = CONTA_CORRENTE,
            chave = "email@email.com",
            tipoDeChave = EMAIL
        )

        // ação
        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, NovaChaveRequest::class.java)


        // validação

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))

    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(
            KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java
        )
    }
}
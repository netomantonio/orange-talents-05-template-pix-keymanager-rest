package br.com.zup.keymanager

import br.com.zup.KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
import br.com.zup.RemoveChaveResponse
import br.com.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var  removeStub: KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve remover uma chave pix`(){
        // cenário
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoveChaveResponse.newBuilder().setInfo("REMOVED SUCCESSFUL").build()

        given(removeStub.remover(Mockito.any())).willReturn(respostaGrpc)

        // ação
        val request = HttpRequest.DELETE<Any>(
            "/api/v1/clientes/$clienteId/pix/$pixId"
        )

        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class RemoveStubFactory {

        @Singleton
        fun deletaChave() = mock(
            KeyManagerRemoveGrpcServiceBlockingStub::class.java
        )
    }
}
package br.com.zup.keymanager

import br.com.zup.*
import br.com.zup.KeyManagerCarregaGrpcServiceGrpc.KeyManagerCarregaGrpcServiceBlockingStub
import br.com.zup.KeyManagerListaGrpcServiceGrpc.KeyManagerListaGrpcServiceBlockingStub
import br.com.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CarregaChavePixControllerTest {
    @field:Inject
    lateinit var carregaChaveStub: KeyManagerCarregaGrpcServiceBlockingStub

    @field:Inject
    lateinit var listaChaveStub: KeyManagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CHAVE_EMAIL = "email@teste.com.br"
    val CHAVE_CELULAR = "+5541988535194"
    val CONTA_CORRENTE = TipoConta.CONTA_CORRENTE
    val TIPO_DE_CHAVE_EMAIL = TipoChave.EMAIL
    val TIPO_DE_CHAVE_CELULAR = TipoChave.CELULAR
    val INSTITUICAO = "ITAU UNIBANCO"
    val TITULAR = "Tone Max"
    val DOCUMENTO_DO_TITULAR = "64470957011"
    val AGENCIA = "0001"
    val NUMERO_DA_CONTA = "50590"
    val CHAVE_CRIADA_EM = LocalDateTime.now()

    @Test
    fun `deve carregar uma chave pix existente`(){
        // cenário
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(carregaChaveStub.carregar(Mockito.any())).willReturn(
            carregaChavePixResponse(clienteId, pixId)
        )

        // ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        // validação

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    private fun carregaChavePixResponse(clienteId: String, pixId: String) =
        CarregaChaveResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChave(CarregaChaveResponse.ChavePix.newBuilder()
                .setTipo(TIPO_DE_CHAVE_EMAIL)
                .setChave(CHAVE_EMAIL)
                .setConta(CarregaChaveResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(CONTA_CORRENTE)
                    .setInstituicao(INSTITUICAO)
                    .setNomeTitular(TITULAR)
                    .setCpfTitular(DOCUMENTO_DO_TITULAR)
                    .setAgencia(AGENCIA)
                    .setNumeroConta(NUMERO_DA_CONTA)
                    .build()
                )
                .setCriadaEm(CHAVE_CRIADA_EM.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
            )
        .build()


    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubListaMock() = mock(
            KeyManagerListaGrpcServiceBlockingStub::class.java
        )

        @Singleton
        fun stubCarregaMock() = mock(
            KeyManagerCarregaGrpcServiceBlockingStub::class.java
        )
    }

}
package br.com.zup.keymanager

import br.com.zup.KeyManagerRegistraGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid


@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraChavePixController(
    private val registraChavePixClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub) {

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun create(clienteId: UUID, @Valid @Body request: NovaChaveRequest) : HttpResponse<Any> {
        LOGGER.info("[$clienteId] criando uma nova chave pix com $request")
        val grpcResponse = registraChavePixClient.registrar(request.toModelGrpc(clienteId))

        return HttpResponse.created(location(clienteId, grpcResponse.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")

}
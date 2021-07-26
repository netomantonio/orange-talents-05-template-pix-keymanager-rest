package br.com.zup.keymanager

import br.com.zup.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.RemoveChaveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController (private val removeChavePixClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(clienteId: String, pixId: String) : HttpResponse<Any> {
        LOGGER.info("[$clienteId] removendo uma chave pix com $pixId")

        removeChavePixClient.remover(RemoveChaveRequest.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()
        )

        return HttpResponse.ok()
    }
}
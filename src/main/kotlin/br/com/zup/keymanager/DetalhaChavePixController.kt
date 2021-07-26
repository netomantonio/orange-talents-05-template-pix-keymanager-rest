package br.com.zup.keymanager

import br.com.zup.*
import br.com.zup.KeyManagerCarregaGrpcServiceGrpc.*
import br.com.zup.KeyManagerListaGrpcServiceGrpc.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

@Controller("/api/v1/clientes/{clienteId}")
class DetalhaChavePixController (
    val carregaChavePixClient: KeyManagerCarregaGrpcServiceBlockingStub,
    val listaChavesPixClient: KeyManagerListaGrpcServiceBlockingStub) {


    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun detalhar(
        clienteId: String,
        pixId: String
    ) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] detalhando chave pix por id: $pixId")

        val response = carregaChavePixClient.carregar(CarregaChaveRequest.newBuilder()
            .setPixId(
                CarregaChaveRequest.FiltroPorPixId.newBuilder()
                    .setClienteId(clienteId)
                    .setPixId(pixId)
                    .build()
            ).build()
        )

        return HttpResponse.ok(DetalheChavePixResponse(response))
    }
}
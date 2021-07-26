package br.com.zup.keymanager

import br.com.zup.*
import br.com.zup.KeyManagerCarregaGrpcServiceGrpc.*
import br.com.zup.KeyManagerListaGrpcServiceGrpc.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import org.slf4j.LoggerFactory

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController (
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

    @Get("/pix/")
    fun lista(clienteId: String) : HttpResponse<Any> {
        LOGGER.info("Listando chaves pix do cliente: $clienteId")

        val pix  = listaChavesPixClient.listar(ListaChavesRequest.newBuilder().setClienteId(clienteId).build())

        val chaves = pix.chavesList.map { ChavePixResponse(it) }

        return HttpResponse.ok(chaves)
    }
}
package br.com.zup.keymanager

import br.com.zup.ListaChavesResponse
import java.time.*

class ChavePixResponse(chavePix: ListaChavesResponse.ChavePix) {

    val pixId = chavePix.pixId
    val chave = chavePix.chave
    val tipoChave = chavePix.tipoChave
    val tipoConta = chavePix.tipoConta
    val criadaEm = chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

}

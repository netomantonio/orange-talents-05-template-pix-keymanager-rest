package br.com.zup.keymanager

import br.com.zup.CarregaChaveResponse
import br.com.zup.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.*

@Introspected
class DetalheChavePixResponse(chaveResponse: CarregaChaveResponse) {

    val pixId = chaveResponse.pixId
    val tipo = chaveResponse.chave.tipo
    val chave = chaveResponse.chave.chave

    val criadaEm = chaveResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (chaveResponse.chave.conta.tipo) {
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NÃO RECONHECIDA"
    }

    val conta = mapOf(
        Pair("tipo", tipoConta),
        Pair("instituição", chaveResponse.chave.conta.instituicao),
        Pair("nomeTitular", chaveResponse.chave.conta.nomeTitular),
        Pair("agencia", chaveResponse.chave.conta.agencia),
        Pair("numeroConta", chaveResponse.chave.conta.numeroConta)
    )



}

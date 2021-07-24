package br.com.zup.keymanager.shared.grpc

import br.com.zup.*
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton


@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeyManagerListaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaChave() = KeyManagerCarregaGrpcServiceGrpc.newBlockingStub(channel)
}
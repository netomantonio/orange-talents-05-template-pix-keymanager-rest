package br.com.zup.keymanager

import io.micronaut.validation.Validated
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.validation.Valid

class TipoDeChaveRequestTest {


    @Nested
    inner class ChaveAleatoriaTest{


        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia`(){

            // cenário
            val tipoDechave = TipoDeChaveRequest.ALEATORIA

            // ação e validação
//            assertTrue(tipoDechave.valida("", context))
//            assertTrue(tipoDechave.valida( null,context))

        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`(){

        }

        fun create(@Valid request: NovaChaveRequest) : Boolean{
            return request.tipoDeConta == TipoDeContaRequest.CONTA_CORRENTE
        }
    }

    @Nested
    inner class CpfTest {

    }

    @Nested
    inner class CelularTest {}

    @Nested
    inner class EmailTest {}


}
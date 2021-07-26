package br.com.zup.keymanager

import br.com.caelum.stella.validation.CPFValidator
import br.com.zup.*
import br.com.zup.keymanager.shared.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.EmailValidator
import java.util.*
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@ValidPixKey
@Introspected
class NovaChaveRequest(
    @field:NotNull val tipoDeConta: TipoDeContaRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoDeChave: TipoDeChaveRequest?
) {

    fun toModelGrpc(clienteId: UUID): RegistraChaveRequest {
        return RegistraChaveRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoConta(tipoDeConta?.atributoGrpc ?: TipoConta.UNKNOWN_TIPO_CONTA)
            .setTipoChave(tipoDeChave?.atributoGrpc ?: TipoChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave ?: "")
            .build()
    }

}

enum class TipoDeChaveRequest(val atributoGrpc: TipoChave){

    CPF(TipoChave.CPF){
        override fun valida(chave: String?, context: ConstraintValidatorContext): Boolean {
            if (chave.isNullOrBlank()) return false

            return CPFValidator(false)
                .invalidMessagesFor(chave)
                .isEmpty()
        }
    },

    CELULAR(TipoChave.CELULAR) {
        override fun valida(chave: String?, context: ConstraintValidatorContext): Boolean {
            if (chave.isNullOrBlank()) return false

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoChave.EMAIL){
        override fun valida(chave: String?, context: ConstraintValidatorContext): Boolean {
            if (chave.isNullOrBlank()) return false

            return EmailValidator().run {
                initialize(null)
                isValid(chave, context)
            }
        }
    },


    ALEATORIA(TipoChave.ALEATORIA){
        override fun valida(chave: String?, context: ConstraintValidatorContext) = chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?, context: ConstraintValidatorContext) : Boolean
}

enum class TipoDeContaRequest( val atributoGrpc: TipoConta){
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}
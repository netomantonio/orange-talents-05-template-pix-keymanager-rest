package br.com.zup.keymanager.shared.validations

import br.com.zup.keymanager.NovaChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import javax.inject.Singleton
import javax.validation.*
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "chave Pix inválida '\${validatedValue.tipoDeChave}'",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NovaChaveRequest> {

    override fun isValid(
        value: NovaChaveRequest?,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value?.tipoDeChave == null) {
            return false
        }

       return value.tipoDeChave.valida(value.chave, context)
    }
}
package com.movies.list.utils.validators.media

import javax.servlet.http.Part
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PartFileExtensionValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class FileExtension(
        val value: Array<String>,
        val message: String = "Данный формат не поддерживается",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class PartFileExtensionValidator : ConstraintValidator<FileExtension, Part> {
    private lateinit var extensions: List<String>

    override fun isValid(file: Part?, context: ConstraintValidatorContext): Boolean {
        file ?: return true
        val name = file.submittedFileName ?: file.name
        if (!name.contains(".")) {
            return false
        }
        val index = name.lastIndexOf(".") + 1
        val extension = name.substring(index)
        return extensions.contains(extension)
    }

    override fun initialize(constraintAnnotation: FileExtension) {
        extensions = constraintAnnotation.value.toList()
    }
}
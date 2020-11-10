package com.movies.list.utils.validators.media

import javax.servlet.http.Part
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PartFileContentTypeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class ContentType(
        val value: Array<String>,
        val message: String = "Данный формат не поддерживается",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class PartFileContentTypeValidator : ConstraintValidator<ContentType, Part> {
    private lateinit var contentTypes: List<String>

    override fun isValid(file: Part?, context: ConstraintValidatorContext) =
            if (file != null) contentTypes.contains(file.contentType) else true

    override fun initialize(constraintAnnotation: ContentType) {
        this.contentTypes = constraintAnnotation.value.toList()
    }
}
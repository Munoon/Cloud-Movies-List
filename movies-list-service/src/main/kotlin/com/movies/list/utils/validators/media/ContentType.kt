package com.movies.list.utils.validators.media

import org.springframework.web.multipart.MultipartFile
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MultipartFileContentTypeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class ContentType(
        val value: Array<String>,
        val message: String = "Данный формат не поддерживается",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class MultipartFileContentTypeValidator : ConstraintValidator<ContentType, MultipartFile> {
    private lateinit var contentTypes: List<String>

    override fun isValid(file: MultipartFile, context: ConstraintValidatorContext) =
            contentTypes.contains(file.contentType)

    override fun initialize(constraintAnnotation: ContentType) {
        this.contentTypes = constraintAnnotation.value.toList()
    }
}
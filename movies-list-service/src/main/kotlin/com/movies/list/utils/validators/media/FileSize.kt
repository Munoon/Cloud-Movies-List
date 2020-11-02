package com.movies.list.utils.validators.media

import org.bson.types.Binary
import org.springframework.util.unit.DataSize
import org.springframework.util.unit.DataUnit
import org.springframework.web.multipart.MultipartFile
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MultipartFileSizeValidator::class, BinaryFileSizeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class FileSize(
        val min: String = "0",
        val max: String = "",
        val message: String = "Файл не соответствует указанным размерам",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

abstract class BaseFileSizeValidator<T> : ConstraintValidator<FileSize, T> {
    private lateinit var min: DataSize
    private var max: DataSize? = null

    override fun isValid(file: T, context: ConstraintValidatorContext): Boolean {
        val size = DataSize.of(getSize(file), DataUnit.BYTES)
        return !(size < min || (max != null && size > max!!))
    }

    override fun initialize(constraintAnnotation: FileSize) {
        this.min = DataSize.parse(constraintAnnotation.min)
        this.max = if (constraintAnnotation.max.isEmpty()) null else DataSize.parse(constraintAnnotation.max)
    }

    abstract fun getSize(file: T): Long
}

class MultipartFileSizeValidator : BaseFileSizeValidator<MultipartFile>() {
    override fun getSize(file: MultipartFile) = file.size
}

class BinaryFileSizeValidator : BaseFileSizeValidator<Binary>() {
    override fun getSize(file: Binary) = file.data.size.toLong()
}
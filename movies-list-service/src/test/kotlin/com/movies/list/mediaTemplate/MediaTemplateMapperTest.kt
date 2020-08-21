package com.movies.list.mediaTemplate

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.Binary
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class MediaTemplateMapperTest {
    @Test
    internal fun asTo() {
        val time = LocalDateTime.now()
        val mediaTemplate = MediaTemplate("TEST_ID", Binary(ByteArray(1)), time)
        val actual = MediaTemplateMapper.INSTANCE.asTo(mediaTemplate)
        assertThat(actual).isEqualToComparingFieldByField(MediaTemplateTo("TEST_ID", time))
    }
}
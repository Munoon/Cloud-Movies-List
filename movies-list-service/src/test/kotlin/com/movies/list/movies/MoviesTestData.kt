package com.movies.list.movies

import com.movies.common.movie.SmallMovieTo
import com.movies.list.utils.JsonUtil
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.web.servlet.ResultMatcher

object MoviesTestData {
    fun assertMatch(actual: Movie?, expected: Movie) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered")
    }

    fun assertMatch(actual: Iterable<Movie>, expected: Iterable<Movie>) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered").isEqualTo(expected)
    }

    fun assertMatch(actual: Iterable<Movie>, vararg expected: Movie) {
        assertMatch(actual, expected.asList())
    }

    fun assertMatch(actual: SmallMovieTo, expected: SmallMovieTo) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered")
    }

    fun jsonContent(expected: SmallMovieTo): ResultMatcher {
        return ResultMatcher {
            val content = JsonUtil.getContent(it)
            val actual = JsonUtil.readValue(content, SmallMovieTo::class.java)
            assertMatch(actual, expected)
        }
    }
}
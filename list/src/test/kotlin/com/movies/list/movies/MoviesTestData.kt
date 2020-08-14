package com.movies.list.movies

import org.assertj.core.api.Assertions.assertThat

object MoviesTestData {
    fun assertMatch(actual: Movie, expected: Movie) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered")
    }

    fun assertMatch(actual: Iterable<Movie>, expected: Iterable<Movie>) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered").isEqualTo(expected)
    }

    fun assertMatch(actual: Iterable<Movie>, vararg expected: Movie) {
        assertMatch(actual, expected.asList())
    }
}
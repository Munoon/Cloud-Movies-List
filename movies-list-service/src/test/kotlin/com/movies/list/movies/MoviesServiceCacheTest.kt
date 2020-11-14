package com.movies.list.movies

import com.movies.list.AbstractTest
import com.movies.list.movies.MoviesTestData.assertMatch
import com.movies.list.movies.to.CreateMovieTo
import com.neovisionaries.i18n.CountryCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDate

internal class MoviesServiceCacheTest : AbstractTest() {
    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var moviesService: MoviesService

    @Test
    fun createMovie() {
        moviesService.createMovie(CreateMovieTo("Test1", "Test1", "about1", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "1+", "1:30"), null)
        cacheManager.getCache("movies")!!.clear()

        val request = PageRequest.of(0, 1)
        val page = moviesService.getPage(request)
        assertThat(cacheManager.getCache("movies_list")!!.get(request, Page::class.java)).isEqualTo(page)

        val findQuery = "test";
        val find = moviesService.findMovieByNameAndOriginalName(findQuery, request)
        val findCacheKey = "${findQuery.hashCode()}-${request.hashCode()}"
        assertThat(cacheManager.getCache("movies_find")!!.get(findCacheKey, Page::class.java)).isEqualTo(find)

        val movie = moviesService.createMovie(CreateMovieTo("Test", "Test", "about", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "1+", "1:30"), null)
        assertMatch(getCache(movie.id!!), movie)
        assertThat(cacheManager.getCache("movies_list")!!.get(request)).isNull()
        assertThat(cacheManager.getCache("movies_find")!!.get(findCacheKey)).isNull()
    }

    @Test
    fun getById() {
        val movie = moviesService.createMovie(CreateMovieTo("Test", "Test", "about", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "1+", "1:30"), null)
        cacheManager.getCache("movies")!!.clear()
        assertThat(getCache(movie.id!!)).isNull()
        moviesService.getById(movie.id!!)
        assertMatch(getCache(movie.id!!), movie)
    }

    @Test
    fun getPage() {
        moviesService.createMovie(CreateMovieTo("Test", "Test", "about", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "1+", "1:30"), null)
        val page = moviesService.getPage(PageRequest.of(0, 1))
        assertThat(cacheManager.getCache("movies_list")!!.get(PageRequest.of(0, 1), Page::class.java))
                .isEqualTo(page)
    }

    @Test
    fun findMovieByNameAndOriginalName() {
        moviesService.createMovie(CreateMovieTo("Test", "Test", "about", CountryCode.US, setOf(MoviesGenres.ACTION), LocalDate.now(), "1+", "1:30"), null)
        val query = "test"
        val request = PageRequest.of(0, 1)
        val find = moviesService.findMovieByNameAndOriginalName(query, request)
        val cacheKey = "${query.hashCode()}-${request.hashCode()}"
        assertThat(cacheManager.getCache("movies_find")!!.get(cacheKey, Page::class.java)).isEqualTo(find)
    }

    private fun getCache(id: String) = cacheManager.getCache("movies")!!.get(id, Movie::class.java)
}
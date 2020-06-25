package org.moviedb.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.moviedb.data.remote.TheMovieDbServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class TheMovieDbServicesTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: TheMovieDbServices

    private lateinit var mockServer: MockWebServer

    @Before
    fun setup(){
        mockServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDbServices::class.java)
    }

    @After
    fun tearDown(){
        mockServer.shutdown()
    }

    @Test
    fun getMovieList(){
        runBlocking {
            getMockResponseFromFile("movie_list.json")
            val resp = service.getPopularMovies().body()
            val movies = resp!!.results
            assertNotNull(movies)
            assertThat(movies!!.size, `is`(10))
        }
    }


    @Test
    fun getDetailMovie(){
        runBlocking {
            getMockResponseFromFile("movie.json")
            val movie = service.getMovieDetail(475557).body()
            assertNotNull(movie)
            assertThat(movie!!.id, `is`(475557))
            assertThat(movie.title, `is`("Joker"))
            assertThat(movie.date, `is`("2019-10-02"))
            assertThat(movie.description, `is`("During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure."))
            assertThat(movie.posterImage, `is`( "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg"))
            assertThat(movie.backgroundImage, `is`("/f5F4cRhQdUbyVbB5lTNCwUzD6BP.jpg"))
        }
    }

    @Test
    fun getGenresFromMovie(){
        runBlocking {
            getMockResponseFromFile("movie.json")
            val resp = service.getMovieDetail(475557).body()
            val genres = resp?.genres
            assertNotNull(genres)
            assertThat(genres!!.size, `is`(3))
            assertThat(genres[0].id, `is`(80))
            assertThat(genres[0].name, `is`("Crime"))
            assertThat(genres[1].id, `is`(53))
            assertThat(genres[1].name, `is`("Thriller"))
            assertThat(genres[2].id, `is`(18))
            assertThat(genres[2].name, `is`("Drama"))
        }
    }


    private fun getMockResponseFromFile(fileName: String){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        val headers: Map<String, String> = emptyMap()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockServer.enqueue(mockResponse.setBody(
            source.readString(Charsets.UTF_8))
        )
    }


}
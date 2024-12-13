package com.spidy.moviesearchapp.data

import com.spidy.moviesearchapp.model.MovieDetailResponse
import com.spidy.moviesearchapp.model.SearchMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    suspend fun searchMovies(@Query("apikey") apikey:String,@Query("s") s:String,@Query("page") page:String):Response<SearchMovieResponse>

    @GET("/")
    suspend fun getMovieDetail(@Query("apikey") apikey:String,@Query("i") s:String):Response<MovieDetailResponse>
}
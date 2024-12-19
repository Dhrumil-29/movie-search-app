package com.spidy.moviesearchapp.data

import com.spidy.moviesearchapp.BuildConfig
import com.spidy.moviesearchapp.model.MovieDetailResponse
import com.spidy.moviesearchapp.model.SearchMovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: ApiService) {
    fun getSearchMovies(s: String, page: Int): Flow<ApiResponse<SearchMovieResponse>> = flow {
        emit(ApiResponse.Loading())
        try {
            val response = apiService.searchMovies(BuildConfig.API_KEY, s, page.toString())
            when {
                response.isSuccessful && response.body() != null -> emit(
                    ApiResponse.Success(
                        response.body()
                    )
                )

                else -> emit(ApiResponse.Error("No results found"))
            }
//            if (response.isSuccessful) {
//                response.body()?.let { emit(ApiResponse.Success(it)) }
//            } else {
//                emit(ApiResponse.Error("Something went wrong"))
//            }
        } catch (e: IOException) {
            emit(ApiResponse.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(ApiResponse.Error("An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun getMovieDetail(id: String): Flow<ApiResponse<MovieDetailResponse>> = flow {
        emit(ApiResponse.Loading())
        try {
            val response = apiService.getMovieDetail(BuildConfig.API_KEY, id)
            when {
                response.isSuccessful && response.body() != null -> {
                    emit(ApiResponse.Success(response.body()))
                }

                else -> emit(ApiResponse.Error("No results found"))
            }
        } catch (e: IOException) {
            emit(ApiResponse.Error("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(ApiResponse.Error("An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}
package com.spidy.moviesearchapp.ui.movieDetailScreen

import com.spidy.moviesearchapp.model.MovieDetailResponse

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchMovieList: MovieDetailResponse? = null,
)
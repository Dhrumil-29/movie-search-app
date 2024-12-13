package com.spidy.moviesearchapp.model

data class SearchMovieResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)
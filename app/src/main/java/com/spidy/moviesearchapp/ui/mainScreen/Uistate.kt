package com.spidy.moviesearchapp.ui.mainScreen

import com.spidy.moviesearchapp.model.Search

data class UiState(
    var isLoading: Boolean = false,
    var error: String? = null,
    val searchMovieList: MutableList<Search> = mutableListOf(),
)
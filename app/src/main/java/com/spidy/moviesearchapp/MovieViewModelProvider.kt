package com.spidy.moviesearchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spidy.moviesearchapp.data.MovieRepository

class MovieViewModelProvider(private val movieRepository: MovieRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}
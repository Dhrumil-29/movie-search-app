package com.spidy.moviesearchapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spidy.moviesearchapp.data.ApiConstant
import com.spidy.moviesearchapp.data.ApiResponse
import com.spidy.moviesearchapp.data.MovieRepository
import com.spidy.moviesearchapp.ui.mainScreen.UiState
import com.spidy.moviesearchapp.ui.movieDetailScreen.MovieDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil


@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var _searchState = MutableLiveData<UiState>(UiState())
    val searchState: LiveData<UiState> = _searchState

    private var _detailState = MutableLiveData<MovieDetailUiState>(MovieDetailUiState())
    val detailState: LiveData<MovieDetailUiState> = _detailState

    private var page = 1
    private var maxPage = 1

    private var job: Job? = null

    fun searchNextPage(search: String) {
        if (page >= maxPage) return
        page++
        job?.cancel()
        job = viewModelScope.launch {
            movieRepository.getSearchMovies(search, page).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> _searchState.value =
                        _searchState.value?.copy(error = response.error)

                    is ApiResponse.Loading -> _searchState.value =
                        _searchState.value?.copy(isLoading = true)

                    is ApiResponse.Success -> {
                        val list = response.result?.Search ?: emptyList()
                        val newList = _searchState.value?.searchMovieList
                        newList?.addAll(list.toMutableList())
                        _searchState.value = UiState(searchMovieList = newList ?: mutableListOf())
                    }
                }
            }
        }
    }

    fun searchMovies(search: String, callNextPage: Boolean = false) {
        if (callNextPage && page < maxPage) page.inc()
        job?.cancel()
        job = viewModelScope.launch {
            delay(300)
            movieRepository.getSearchMovies(search, page).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> _searchState.value = UiState(error = response.error)
                    is ApiResponse.Loading -> _searchState.value = UiState(isLoading = true)
                    is ApiResponse.Success -> {
                        _searchState.value =
                            UiState(
                                searchMovieList = response.result?.Search?.toMutableList()
                                    ?: mutableListOf()
                            )
                        response.result?.totalResults?.toInt()
                            ?.let { maxPage = ceil(it.div(ApiConstant.ITEM_PER_PAGE)).toInt() }
                    }
                }
            }
        }
    }

    fun getMovieDetail(id: String) {
        viewModelScope.launch {
            movieRepository.getMovieDetail(id).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> _detailState.value =
                        MovieDetailUiState(error = response.error)

                    is ApiResponse.Loading -> _detailState.value =
                        MovieDetailUiState(isLoading = true)

                    is ApiResponse.Success -> _detailState.value =
                        MovieDetailUiState(searchMovieList = response.result)
                }
            }
        }
    }
}
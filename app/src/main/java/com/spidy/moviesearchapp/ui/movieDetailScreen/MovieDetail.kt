package com.spidy.moviesearchapp.ui.movieDetailScreen

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.spidy.moviesearchapp.MovieViewModel
import com.spidy.moviesearchapp.R
import com.spidy.moviesearchapp.databinding.ActivityMovieDetailBinding
import com.spidy.moviesearchapp.model.MovieDetailResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetail : AppCompatActivity() {

    private var movieId: String = ""

    companion object {
        const val MOVIE_ID = "movieId"
    }

    private lateinit var binding: ActivityMovieDetailBinding

//    private val apiService = RetrofitClient.apiService
//    private val repository = MovieRepository(apiService)
//    private val viewModel by lazy {
//        ViewModelProvider(this, MovieViewModelProvider(repository))[MovieViewModel::class.java]
//    }
    private val viewModel:MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getStringExtra(MOVIE_ID) ?: ""
        viewModel.getMovieDetail(movieId)
        observeData()
    }

    private fun observeData() {
        viewModel.detailState.observe(this) { state ->
            if (state.isLoading) {
                binding.loading.visibility = View.VISIBLE
            } else if (state.error != null) {
                binding.loading.visibility = View.GONE
                binding.error.visibility = View.VISIBLE
                binding.error.text = state.error
            } else {
                binding.loading.visibility = View.GONE
                state.searchMovieList?.let { setMovieData(it) }
            }
        }

    }

    private fun setMovieData(movieData: MovieDetailResponse) {
        binding.title.text = movieData.Title
        Glide.with(this)
            .load(movieData.Poster)
            .error(R.drawable.ic_launcher_background)
            .into(binding.poster)
        binding.plot.text = "Plot : ${movieData.Plot}"
        binding.year.text = "Year : ${movieData.Year}"
        binding.director.text = "Director : ${movieData.Director}"
        binding.runtime.text = "Time : ${movieData.Runtime}"
    }
}
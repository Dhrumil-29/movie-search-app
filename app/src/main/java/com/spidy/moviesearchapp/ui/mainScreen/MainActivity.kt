package com.spidy.moviesearchapp.ui.mainScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spidy.moviesearchapp.MovieViewModel
import com.spidy.moviesearchapp.MovieViewModelProvider
import com.spidy.moviesearchapp.data.MovieRepository
import com.spidy.moviesearchapp.data.RetrofitClient
import com.spidy.moviesearchapp.databinding.ActivityMainBinding
import com.spidy.moviesearchapp.ui.movieDetailScreen.MovieDetail

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val apiService by lazy { RetrofitClient.apiService }
    private val repository by lazy { MovieRepository(apiService) }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MovieViewModelProvider(repository)
        )[MovieViewModel::class.java]
    }
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = layoutManager
        movieAdapter = MovieAdapter { movieId ->
            navToMovieDetail(movieId)
        }
        binding.rv.adapter = movieAdapter

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                println("$totalItemCount, $lastVisibleItem, $VISIBLE_THRESHOLD")
                if (binding.loading.visibility != View.VISIBLE && lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
                    viewModel.searchNextPage(binding.search.query.toString())
                    binding.loading.visibility = View.VISIBLE
                }
            }
        })
        observeData()
    }

    private fun navToMovieDetail(movieId: String) {
        val intent = Intent(this, MovieDetail::class.java)
        intent.putExtra(MovieDetail.MOVIE_ID, movieId)
        startActivity(intent)
    }

    private fun observeData() {
        viewModel.searchState.observe(this) { state ->
            if (state.isLoading) {
                binding.loading.visibility = View.VISIBLE
            }
            if (state.error != null) {
                binding.loading.visibility = View.GONE
                Toast.makeText(this@MainActivity, state.error, Toast.LENGTH_SHORT).show()
            } else {
                binding.loading.visibility = View.GONE
                binding.error.visibility = View.GONE
                movieAdapter.submitList(state.searchMovieList.toList())
                if (state.searchMovieList.isEmpty() && binding.search.query.isNotEmpty()) {
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = "No Movies found for \"${binding.search.query}\""
                }
            }
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.trim()?.let {
                    viewModel.searchMovies(it)
                }
                return true
            }

        })
    }

    companion object {
        const val VISIBLE_THRESHOLD = 3
    }
}

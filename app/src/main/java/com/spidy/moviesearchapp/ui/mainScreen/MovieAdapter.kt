package com.spidy.moviesearchapp.ui.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spidy.moviesearchapp.R
import com.spidy.moviesearchapp.databinding.RowMovieBinding
import com.spidy.moviesearchapp.model.Search

class MovieAdapter(val onClick: (String) -> Unit) :
    ListAdapter<Search, MovieAdapter.MovieHolder>(MovieDiffCallback()) {
    inner class MovieHolder(itemBiding: RowMovieBinding) :
        RecyclerView.ViewHolder(itemBiding.root) {
        private val title: TextView = itemBiding.title
        private val type: TextView = itemBiding.type
        private val year: TextView = itemBiding.year
        private val poster: ImageView = itemBiding.poster
        fun bind(movieData: Search) {
            title.text = movieData.Title
            type.text = "Type: ${movieData.Type}"
            year.text = "Year: ${movieData.Year}"
            Glide.with(itemView.context)
                .load(movieData.Poster)
                .error(R.drawable.ic_launcher_background)
                .into(poster)
            itemView.setOnClickListener {
                onClick(movieData.imdbID)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = RowMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Search>() {
    override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem == newItem
    }
}
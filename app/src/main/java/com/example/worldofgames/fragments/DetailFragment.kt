package com.example.worldofgames.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldofgames.MainActivity
import com.example.worldofgames.MainActivity.Companion.gameID
import com.example.worldofgames.R
import com.example.worldofgames.adapters.VideoAdapter
import com.example.worldofgames.data.FavouriteGame
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.MainViewModel
import com.example.worldofgames.utils.JSONUtils
import com.example.worldofgames.utils.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.view.*


class DetailFragment : Fragment(), VideoAdapter.OnPlayClickListener {

    private lateinit var videoAdapter: VideoAdapter
    private lateinit var viewModel: MainViewModel
    private var gameId: Int = 0
    private lateinit var game: Game
    private var favouriteGame: FavouriteGame? = null
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(MainViewModel::class.java)
        gameId = gameID
        game = viewModel.getGameById(gameId)

        button = view.button

        Picasso.get().load(game.coverUrl).into(view.imageViewBigCover)
        view.progressBarRatingRing.progress = game.rating
        view.textViewRating.text = game.rating.toString()
        view.textViewTitle.text = game.name
        view.textViewValueGenres.text = game.genres.joinToString(", ")
        view.textViewValueReleaseDate.text = game.releaseDate
        view.textViewValueAgeRating.text = game.ageRating
        view.textViewSummary.text = game.summary

        setFavourite()

        button.setOnClickListener {
            if (favouriteGame == null) {
                viewModel.insertFavouriteGame(FavouriteGame(game))
            } else {
                viewModel.deleteFavouriteGame(favouriteGame!!)
            }
            setFavourite()
        }

        videoAdapter = VideoAdapter(this)
        view.recyclerViewVideos.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        view.recyclerViewVideos.adapter = videoAdapter

        if (NetworkUtils.hasConnection(requireActivity().applicationContext)) {
            val jsonVideosArray = NetworkUtils.getVideosOfTheGame(gameId)
            val videos = JSONUtils.getVideosFromJSON(jsonVideosArray)
            videoAdapter.videos = videos
        }

        return view
    }

    override fun onPlayClick(url: String) {
        val intentToVideo = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intentToVideo)
    }

    private fun setFavourite() {
        favouriteGame = viewModel.getFavouriteGameById(gameId)
        if (favouriteGame == null) {
            button.text = getString(R.string.add_to_favourite)
        } else {
            button.text = getString(R.string.remove_from_favourite)
        }
    }

}
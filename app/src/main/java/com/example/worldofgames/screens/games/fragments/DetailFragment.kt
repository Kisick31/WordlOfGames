package com.example.worldofgames.screens.games.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldofgames.R
import com.example.worldofgames.adapters.VideoAdapter
import com.example.worldofgames.constants.GameType
import com.example.worldofgames.databinding.FragmentDetailBinding
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.screens.games.MainActivity.Companion.gameType
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class DetailFragment : Fragment(), VideoAdapter.OnPlayClickListener {

    private lateinit var videoAdapter: VideoAdapter
    private lateinit var viewModel: GameViewModel
    private var gameId: Int = 0
    private var favouriteGame: FavouriteGame? = null
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false
            )

        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(GameViewModel::class.java)
        gameId = gameID
        button = binding.button
        videoAdapter = VideoAdapter(this)
        binding.recyclerViewVideos.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        binding.recyclerViewVideos.adapter = videoAdapter

        GlobalScope.launch(Dispatchers.Main) {
            val game = async(Dispatchers.IO) {
                if (gameType == GameType.SIMPLE_GAME)
                    viewModel.getGameById(gameId)
                else
                    viewModel.getHypeGameById(gameId)
            }

            setViews(game, binding)
            setFavourite()
            val gameItem = game.await()
            button.setOnClickListener {
                if (favouriteGame == null) {
                    viewModel.insertFavouriteGame(FavouriteGame(gameItem))
                } else {
                    viewModel.deleteFavouriteGame(favouriteGame!!)
                }
                setFavourite()
            }

            val videos = viewModel.getVideosOfTheGame(gameId)
            videoAdapter.videoItems = videos

        }

        return binding.root
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

    private suspend fun setViews(game: Deferred<GameItem>, binding: FragmentDetailBinding) {

        while (!game.isCompleted){
            binding.progressBarLoading.visibility = View.VISIBLE
        }
        val gameItem = game.await()
        button.visibility = View.VISIBLE
        binding.progressBarRatingRing.visibility = View.VISIBLE
        binding.textViewLabelAgeRating.visibility = View.VISIBLE
        binding.textViewLabelGenres.visibility = View.VISIBLE
        binding.textViewLabelReleaseDate.visibility = View.VISIBLE
        binding.progressBarLoading.visibility = View.GONE
        Picasso.get().load("https://images.igdb.com/igdb/image/upload/t_cover_big_2x/" +
                gameItem.cover.imageId + ".jpg").into(binding.imageViewBigCover)
        binding.progressBarRatingRing.progress = gameItem.rating.roundToInt()
        binding.textViewRating.text = gameItem.rating.roundToInt().toString()
        binding.textViewTitle.text = gameItem.name
        binding.textViewValueGenres.text = run {
            val genres = arrayListOf<String>()
            for (genre in gameItem.genres) {
                genres.add(genre.name)
            }
            genres.joinToString(", ")
        }
        binding.textViewValueReleaseDate.text = gameItem.releaseDates[0].human
        binding.textViewValueAgeRating.text = if (gameItem.ageRatings?.isNotEmpty() == true) {
            getAgeRating(gameItem.ageRatings!![0].rating)
        } else {
            resources.getString(R.string.rating_not_specified)
        }
        binding.textViewSummary.text = gameItem.summary

    }

    private fun getAgeRating(rating: Int): String = when (rating) {
        1 -> resources.getString(R.string.pegi_three)
        2 -> resources.getString(R.string.pegi_seven)
        3 -> resources.getString(R.string.pegi_twelve)
        4 -> resources.getString(R.string.pegi_sixteen)
        5 -> resources.getString(R.string.pegi_eighteen)
        6 -> resources.getString(R.string.rating_expected)
        7 -> resources.getString(R.string.early_childhood)
        8 -> resources.getString(R.string.everyone)
        9 -> resources.getString(R.string.everyone_10_and_older)
        10 -> resources.getString(R.string.teen)
        11 -> resources.getString(R.string.mature)
        12 -> resources.getString(R.string.adults_only_18)
        else -> resources.getString(R.string.rating_not_specified)
    }
}
package com.example.worldofgames.screens.games.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldofgames.R
import com.example.worldofgames.adapters.VideoAdapter
import com.example.worldofgames.constants.GameType
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.screens.games.MainActivity.Companion.gameType
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.view.*
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(GameViewModel::class.java)
        gameId = gameID
        button = view.findViewById(R.id.button)
        videoAdapter = VideoAdapter(this)
        view.recyclerViewVideos.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        view.recyclerViewVideos.adapter = videoAdapter

        GlobalScope.launch(Dispatchers.Main) {
            val game = async(Dispatchers.IO) {
                if (gameType == GameType.SIMPLE_GAME)
                    viewModel.getGameById(gameId)
                else
                    viewModel.getHypeGameById(gameId)
            }

            setViews(game, view)
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

    private suspend fun setViews(game: Deferred<GameItem>, view: View) {

        while (!game.isCompleted){
            view.progressBarLoading.visibility = View.VISIBLE
        }
        button.visibility = View.VISIBLE
        view.progressBarRatingRing.visibility = View.VISIBLE
        view.textViewLabelAgeRating.visibility = View.VISIBLE
        view.textViewLabelGenres.visibility = View.VISIBLE
        view.textViewLabelReleaseDate.visibility = View.VISIBLE
        view.progressBarLoading.visibility = View.GONE

        Picasso.get().load("https://images.igdb.com/igdb/image/upload/t_cover_big_2x/" +
                game.await().cover.imageId + ".jpg").into(view.imageViewBigCover)
        view.progressBarRatingRing.progress = game.await().rating.roundToInt()
        view.textViewRating.text = game.await().rating.roundToInt().toString()
        view.textViewTitle.text = game.await().name
        view.textViewValueGenres.text = run {
            val genres = arrayListOf<String>()
            for (genre in game.await().genres) {
                genres.add(genre.name)
            }
            genres.joinToString(",")
        }
        view.textViewValueReleaseDate.text = game.await().releaseDates[0].human
        view.textViewValueAgeRating.text = if (game.await().ageRatings?.isNotEmpty() == true) {
            getAgeRating(game.await().ageRatings!![0].rating)
        } else {
            resources.getString(R.string.rating_not_specified)
        }
        view.textViewSummary.text = game.await().summary

    }

    private fun getAgeRating(rating: Int): String = when (rating) {
        1 -> resources.getString(R.string.pegi_three)
        2 -> resources.getString(R.string.pegi_seven)
        3 -> resources.getString(R.string.pegi_twelve)
        4 -> resources.getString(R.string.pegi_sixteen)
        5 -> resources.getString(R.string.pegi_eighteen)
        6 -> resources.getString(R.string.rating_exepted)
        7 -> resources.getString(R.string.early_childhood)
        8 -> resources.getString(R.string.everyone)
        9 -> resources.getString(R.string.everyone_10_and_older)
        10 -> resources.getString(R.string.teen)
        11 -> resources.getString(R.string.mature)
        12 -> resources.getString(R.string.adults_only_18)
        else -> resources.getString(R.string.rating_not_specified)
    }
}
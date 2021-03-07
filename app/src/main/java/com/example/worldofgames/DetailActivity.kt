package com.example.worldofgames

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldofgames.adapters.VideoAdapter
import com.example.worldofgames.data.FavouriteGame
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.MainViewModel
import com.example.worldofgames.utils.JSONUtils
import com.example.worldofgames.utils.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(),
    VideoAdapter.OnPlayClickListener {

    private lateinit var videoAdapter: VideoAdapter
    private lateinit var viewModel: MainViewModel
    private var gameId: Int = 0
    private var game: Game? = null
    private var favouriteGame: FavouriteGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainViewModel::class.java)

        if (intent.hasExtra("id")) {
            gameId = intent.getIntExtra("id", -1)
            game = viewModel.getGameById(gameId)
        } else {
            finish()
        }

        Picasso.get().load(game?.coverUrl).into(imageViewBigCover)
        progressBarRatingRing.progress = game?.rating!!
        textViewRating.text = game?.rating.toString()
        textViewTitle.text = game?.name
        textViewValueGenres.text = game?.genres?.joinToString(", ")
        textViewValueReleaseDate.text = game?.releaseDate
        textViewValueAgeRating.text = game?.ageRating
        textViewSummary.text = game?.summary
        setFavourite()
        videoAdapter = VideoAdapter(this)
        recyclerViewVideos.layoutManager = LinearLayoutManager(this)
        recyclerViewVideos.adapter = videoAdapter
        if (NetworkUtils.hasConnection(this)) {
            val jsonArrayVideos = NetworkUtils.getVideosOfTheGame(gameId)
            val videos = JSONUtils.getVideosFromJSON(jsonArrayVideos)
            videoAdapter.videos = videos
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemMain -> {
                val intentToMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentToMainActivity)
            }
            R.id.itemFavourite -> {
                val intentToFavouriteActivity = Intent(this, FavouriteActivity::class.java)
                startActivity(intentToFavouriteActivity)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun onClickAddToFavourite(view: View?) {
        if (favouriteGame == null) {
            viewModel.insertFavouriteGame(FavouriteGame(game!!))
        } else {
            viewModel.deleteFavouriteGame(favouriteGame!!)
        }
        setFavourite()
    }

    private fun setFavourite() {
        favouriteGame = viewModel.getFavouriteGameById(gameId)
        if (favouriteGame == null) {
            button.text = getString(R.string.add_to_favourite)
        } else {
            button.text = getString(R.string.remove_from_favourite)
        }
    }

    override fun onPlayClick(url: String) {
        val intentToVideo = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intentToVideo)
    }
}
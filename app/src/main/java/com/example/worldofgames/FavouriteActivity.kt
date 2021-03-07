package com.example.worldofgames

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.MainViewModel
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity(), GamesAdapter.OnCoverClickListener {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        recyclerViewFavouriteGames.layoutManager = GridLayoutManager(this, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewFavouriteGames.adapter = gamesAdapter
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)
        val favouriteGames = viewModel.favouriteGames
        favouriteGames.observe(this, {
            gamesAdapter.games = it
        })
    }

    override fun onCoverClick(position: Int, game: Game) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("id", game.id)
        startActivity(intent)
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
}
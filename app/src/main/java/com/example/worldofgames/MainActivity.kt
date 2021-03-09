package com.example.worldofgames

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.utils.NetworkUtils
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.MainViewModel
import com.example.worldofgames.utils.JSONUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity(), GamesAdapter.OnCoverClickListener, LoaderManager.LoaderCallbacks<JSONArray> {

    private lateinit var viewModel: MainViewModel
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var loaderManager: LoaderManager

    companion object {
        private const val LOADER_ID = 1321
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loaderManager = LoaderManager.getInstance(this)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)

        recyclerViewCover.layoutManager = GridLayoutManager(this, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewCover.adapter = gamesAdapter
        if(NetworkUtils.hasConnection(this)){
            downloadData()
        }
        val gamesFromLiveData = viewModel.games
        gamesFromLiveData.observe(this, {
            gamesAdapter.games = it
        })
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

    override fun onCoverClick(position: Int, game: Game) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("id", game.id)
        startActivity(intent)
    }

    private fun downloadData(){
        loaderManager.restartLoader(LOADER_ID, null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<JSONArray> {
        return NetworkUtils.Companion.JSONGamesLoader(this)
    }

    override fun onLoadFinished(loader: Loader<JSONArray>, data: JSONArray) {
        val games = JSONUtils.getGamesFromJSON(data, this)
        if (games.isNotEmpty()) {
            viewModel.deleteAllGames()
            for (game in games) {
                viewModel.insertGame(game)
            }
        }
        loaderManager.destroyLoader(LOADER_ID)
    }

    override fun onLoaderReset(loader: Loader<JSONArray>) {

    }


}
package com.example.worldofgames.screens.games

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.worldofgames.data.GameDatabase
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.hype_games.HypeGameItem
import com.example.worldofgames.enteties.hype_games.HypeGameList
import com.example.worldofgames.enteties.games.GameList
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.enteties.videos.VideoList
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.*

class GameViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val BODY = "fields name, cover.image_id, screenshots.image_id," +
                "genres.name, age_ratings.rating, rating, release_dates.human," +
                "release_dates.platform, platforms.name, summary;"
        private const val SORT_TOP = "sort rating desc; " +
                "where rating != null & rating_count>70 & version_parent = null & parent_game = null; " +
                "l 100;"
        private const val SORT_ON_HYPE = "sort hypes desc; " +
                "where hypes > 100; " +
                "l 20;"
        private const val CLIENT_ID = "2va411riu61eplp40btvmb5obk7rzo"
        private const val AUTHORIZATION = "Bearer toytxz5vr5ezf3lgrdjvpap5hnrc3e"
        private const val ACCEPT = "application/json"
    }

    private val mutableIsDataLoading: MutableLiveData<Boolean?> = MutableLiveData()
    private val mutableErrors = MutableLiveData<FuelError?>()

    private var db = GameDatabase.getInstance(application)
    val games = db.gamesDao().getAllGames()
    val favouriteGames = db.gamesDao().getAllFavouriteGames()
    val isDataLoading: LiveData<Boolean?> = mutableIsDataLoading
    val errors: LiveData<FuelError?> = mutableErrors

    fun getGameById(gameId: Int): GameItem {
        return db.gamesDao().getGameById(gameId)
    }

    fun getHypeGameById(gameId: Int): GameItem {
        return db.gamesDao().getHypeGameById(gameId)
    }

    fun getFavouriteGameById(gameId: Int): GameItem {
        return db.gamesDao().getFavouriteGameById(gameId)
    }

    fun getFavouriteGame(gameId: Int): FavouriteGame? = runBlocking {
        var game: FavouriteGame? = null

        CoroutineScope(Dispatchers.IO).launch {
            game = db.gamesDao().getFavouriteGameById(gameId)
        }.join()

        return@runBlocking game
    }

    fun insertFavouriteGame(favGame: FavouriteGame) = runBlocking {
        CoroutineScope(Dispatchers.IO).launch {
            db.gamesDao().insertFavouriteGame(favGame)
        }.join()
    }


    fun deleteFavouriteGame(favGame: FavouriteGame) = runBlocking {
        CoroutineScope(Dispatchers.IO).launch {
            db.gamesDao().deleteFavouriteGame(favGame)
        }.join()

    }

    fun getOnHypeGames(): ArrayList<HypeGameItem> = runBlocking(Dispatchers.Default){

        var gamesItems = ArrayList<HypeGameItem>()
            "https://api.igdb.com/v4/games".httpPost()
                .header(
                    "Client-ID" to CLIENT_ID,
                    "Authorization" to AUTHORIZATION,
                    "Accept" to ACCEPT
                )
                .body(BODY + SORT_ON_HYPE).responseString { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            mutableErrors.value = ex
                        }
                        is Result.Success -> {
                            val data = result.get()
                            gamesItems = Gson().fromJson(data, HypeGameList::class.java)
                            db.gamesDao().deleteAllHypeGames()
                            db.gamesDao().insertHypeGames(gamesItems)
                        }
                    }
                }.join()
            return@runBlocking gamesItems
        }


    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableIsDataLoading.postValue(true)
            "https://api.igdb.com/v4/games".httpPost()
                .header(
                    "Client-ID" to CLIENT_ID,
                    "Authorization" to AUTHORIZATION,
                    "Accept" to ACCEPT
                )
                .body(BODY + SORT_TOP).responseString { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            mutableErrors.value = ex
                        }
                        is Result.Success -> {
                            val data = result.get()
                            val gamesItems = Gson().fromJson(data, GameList::class.java)
                            db.gamesDao().deleteAllGames()
                            db.gamesDao().insertGames(gamesItems)
                        }
                    }
                }
            mutableIsDataLoading.postValue(false)
        }
    }


    fun getVideosOfTheGame(gameId: Int): VideoList = runBlocking {
        var videoJsonString: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            val (_, response, result) = "https://api.igdb.com/v4/game_videos".httpPost()
                .header(
                    "Client-ID" to CLIENT_ID,
                    "Authorization" to AUTHORIZATION,
                    "Accept" to ACCEPT
                )
                .body("fields name,video_id; where game = $gameId;")
                .responseString()
            if (response.isSuccessful) {
                videoJsonString = result.component1()
            }
        }.join()
        return@runBlocking Gson().fromJson(videoJsonString, VideoList::class.java)
    }
}

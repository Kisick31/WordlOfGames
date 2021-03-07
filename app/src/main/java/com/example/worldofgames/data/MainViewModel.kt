package com.example.worldofgames.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class   MainViewModel(application: Application) : AndroidViewModel(application) {

    var games: LiveData<List<Game>>
    var favouriteGames: LiveData<List<FavouriteGame>>

    companion object{
        private lateinit var database: GameDatabase

        private class InsertGameTask : AsyncTask<Game, Void, Void?>() {
            override fun doInBackground(vararg params: Game?): Void? {
                if(params.isNotEmpty()){
                    params[0]?.let { database.gamesDao().insertGame(it) }
                }
                return null
            }
        }

        private class DeleteGameTask : AsyncTask<Game, Void, Void?>() {
            override fun doInBackground(vararg params: Game?): Void? {
                if(params.isNotEmpty()){
                    params[0]?.let { database.gamesDao().deleteGame(it) }
                }
                return null
            }
        }

        private class GetGameTask : AsyncTask<Int, Void, Game?>() {
            override fun doInBackground(vararg params: Int?): Game? {
                if(params.isNotEmpty()){
                    return params[0]?.let { database.gamesDao().getGameById(it) }
                }
                return null
            }
        }

        private class DeleteGamesTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                database.gamesDao().deleteAllGames()
                return null
            }
        }

        private class InsertFavouriteGameTask : AsyncTask<FavouriteGame, Void, Void?>() {
            override fun doInBackground(vararg params: FavouriteGame?): Void? {
                if(params.isNotEmpty()){
                    params[0]?.let { database.gamesDao().insertFavouriteGame(it) }
                }
                return null
            }
        }

        private class DeleteFavouriteGameTask : AsyncTask<FavouriteGame, Void, Void?>() {
            override fun doInBackground(vararg params: FavouriteGame?): Void? {
                if(params.isNotEmpty()){
                    params[0]?.let { database.gamesDao().deleteFavouriteGame(it) }
                }
                return null
            }
        }

        private class GetFavouriteGameTask : AsyncTask<Int, Void, FavouriteGame?>() {
            override fun doInBackground(vararg params: Int?): FavouriteGame? {
                if(params.isNotEmpty()){
                    return params[0]?.let { database.gamesDao().getFavouriteGameById(it) }
                }
                return null
            }
        }
    }

    init {
        database = GameDatabase.getInstance(getApplication())
        games = database.gamesDao().getAllGames()
        favouriteGames = database.gamesDao().getAllFavouriteGames()
    }

    fun insertGame(game: Game){
        InsertGameTask().execute(game)
    }

    fun deleteGame(game: Game){
        DeleteGameTask().execute(game)
    }

    fun getGameById(id: Int): Game? {
        return GetGameTask().execute(id).get()
    }

    fun deleteAllGames(){
        DeleteGamesTask().execute()
    }

    fun insertFavouriteGame(favouriteGame: FavouriteGame){
        InsertFavouriteGameTask().execute(favouriteGame)
    }

    fun deleteFavouriteGame(favouriteGame: FavouriteGame){
        DeleteFavouriteGameTask().execute(favouriteGame)
    }

    fun getFavouriteGameById(id: Int): FavouriteGame? {
        return GetFavouriteGameTask().execute(id).get()
    }
}
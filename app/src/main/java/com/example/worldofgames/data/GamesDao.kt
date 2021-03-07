package com.example.worldofgames.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GamesDao {
    @Query("SELECT * FROM favourite_games")
    fun getAllFavouriteGames(): LiveData<List<FavouriteGame>>

    @Query("SELECT * FROM game")
    fun getAllGames() : LiveData<List<Game>>

    @Query("SELECT * FROM game WHERE id == :gameId")
    fun getGameById(gameId: Int): Game

    @Query("DELETE FROM game")
    fun deleteAllGames()

    @Insert
    fun insertGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Insert
    fun insertFavouriteGame(favouriteGame: FavouriteGame)

    @Delete
    fun deleteFavouriteGame(favouriteGame: FavouriteGame)

    @Query("SELECT * FROM favourite_games WHERE id == :gameId")
    fun getFavouriteGameById(gameId: Int): FavouriteGame

}
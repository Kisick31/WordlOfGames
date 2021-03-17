package com.example.worldofgames.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.hype_games.HypeGameItem
import com.example.worldofgames.enteties.games.GameItem


@Dao
interface GamesDao {

    @Query("SELECT * FROM games")
    fun getAllGames() : LiveData<List<GameItem>>

    @Query("SELECT * FROM games WHERE id == :gameId")
    fun getGameById(gameId: Int?): GameItem

    @Query("DELETE FROM games")
    fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(games: List<GameItem>)

    @Delete
    fun deleteGame(game: GameItem)

    @Query("SELECT * FROM fav_games")
    fun getAllFavouriteGames(): LiveData<List<FavouriteGame>>

    @Insert
    fun insertFavouriteGame(favouriteGame: FavouriteGame)

    @Delete
    fun deleteFavouriteGame(favouriteGame: FavouriteGame)

    @Query("SELECT * FROM fav_games WHERE id == :gameId")
    fun getFavouriteGameById(gameId: Int): FavouriteGame

    @Query("SELECT * FROM hype_games")
    fun getAllHypeGames() : LiveData<List<HypeGameItem>?>

    @Query("DELETE FROM hype_games")
    fun deleteAllHypeGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHypeGames(gameItems: List<HypeGameItem>)

    @Query("SELECT * FROM hype_games WHERE id == :gameId")
    fun getHypeGameById(gameId: Int?): GameItem
}
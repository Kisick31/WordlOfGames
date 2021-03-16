package com.example.worldofgames.data

import android.content.Context
import androidx.room.*
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.games.GameItem


@Database(entities = [GameItem::class, FavouriteGame::class], version = 3, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gamesDao(): GamesDao

    companion object {
        private const val DB_NAME: String = "games.db"
        private var database: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase {
            if (database == null) {
                synchronized(this) {
                    if (database == null) {
                        database =
                            Room.databaseBuilder(context, GameDatabase::class.java, DB_NAME)
                                .fallbackToDestructiveMigration().build()
                    }
                }
            }
            return database as GameDatabase
        }
    }
}
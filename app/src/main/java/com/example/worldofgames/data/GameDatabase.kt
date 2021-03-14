package com.example.worldofgames.data

import android.content.Context
import androidx.room.*
import com.example.worldofgames.converters.TypeConverter
import com.example.worldofgames.enteties.FavouriteGame
import com.example.worldofgames.enteties.Game


@Database(entities = [Game::class, FavouriteGame::class], version = 4, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gamesDao(): GamesDao

    companion object {
        private const val DB_NAME: String = "games.db"
        private var database: GameDatabase? = null
        private val LOCK: Any = Any()

        fun getInstance(context: Context): GameDatabase {

            if (database == null) {
                synchronized(LOCK) {
                    database = Room.databaseBuilder(context, GameDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration().build()
                }
            }
            return database!!
        }
    }


}
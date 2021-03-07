package com.example.worldofgames.data

import android.content.Context
import androidx.room.*


@Database(entities = [Game::class, FavouriteGame::class], version = 3, exportSchema = false)
@TypeConverters(MyTypeConverter::class)
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
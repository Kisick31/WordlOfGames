package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.Genre
import com.google.gson.Gson
import org.json.JSONArray

class GenreTC {
    @TypeConverter
    fun listGenreToString(listGenre: List<Genre>): String {
        return Gson().toJson(listGenre)
    }

    @TypeConverter
    fun stringToListGenre(string: String): List<Genre> {
        return try {
            val jsonArray = JSONArray(string)
            val list = ArrayList<Genre>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(Genre(jsonObject.getInt("id"), jsonObject.getString("name")))
            }
            list
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
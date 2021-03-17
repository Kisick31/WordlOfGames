package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.Cover
import com.google.gson.Gson

class CoverTC {
    @TypeConverter
    fun coverToString(cover: Cover): String {
        return Gson().toJson(cover)
    }

    @TypeConverter
    fun stringToCover(string: String): Cover {
        val gson = Gson()
        val cover = gson.fromJson(string, Cover::class.java)
        return cover
    }
}
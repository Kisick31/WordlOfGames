package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.Platform
import com.google.gson.Gson
import org.json.JSONArray

class PlatformTC {
    @TypeConverter
    fun listPlatformToString(listPlatform: List<Platform>): String {
        return Gson().toJson(listPlatform)
    }

    @TypeConverter
    fun stringToListPlatform(string: String): List<Platform> {
        return try {
            val jsonArray = JSONArray(string)
            val list = ArrayList<Platform>()
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(Platform(jsonObject.getInt("id"), jsonObject.getString("name")))
            }
            list
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
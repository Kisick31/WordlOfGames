package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.ReleaseDate
import com.google.gson.Gson
import org.json.JSONArray

class ReleaseDateTC {
    @TypeConverter
    fun listReleaseDateToString(listReleaseDate: List<ReleaseDate>): String {
        return Gson().toJson(listReleaseDate)
    }

    @TypeConverter
    fun stringToListReleaseDate(string: String): List<ReleaseDate> {
        return try {
            val jsonArray = JSONArray(string)
            val list = ArrayList<ReleaseDate>()
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(ReleaseDate(jsonObject.getInt("platform"), jsonObject.getString("human"),jsonObject.getInt("id")))
            }
            list
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
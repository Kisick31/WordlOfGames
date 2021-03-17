package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.AgeRating
import com.google.gson.Gson
import org.json.JSONArray

class AgeRatingTC {
    @TypeConverter
    fun listAgeRatingToString(listAgeRating: List<AgeRating>?): String {
        return Gson().toJson(listAgeRating)
    }

    @TypeConverter
    fun stringToListAgeRating(string: String): List<AgeRating> {
        return try {
            val jsonArray = JSONArray(string)
            val list = ArrayList<AgeRating>()
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(AgeRating(jsonObject.getInt("id"), jsonObject.getInt("rating")))
            }
            list
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
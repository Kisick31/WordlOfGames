package com.example.worldofgames.converters

import androidx.room.TypeConverter
import com.example.worldofgames.enteties.games.Screenshot
import com.google.gson.Gson
import org.json.JSONArray

class ScreenshotTC {
    @TypeConverter
    fun listScreenshotToString(listScreenshot: List<Screenshot>): String {
        return Gson().toJson(listScreenshot)
    }

    @TypeConverter
    fun stringToListScreenshot(string: String): List<Screenshot> {
        return try {
            val jsonArray = JSONArray(string)
            val list = ArrayList<Screenshot>()
            for (i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(Screenshot(jsonObject.getInt("id"), jsonObject.getString("image_id")))
            }
            list
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}
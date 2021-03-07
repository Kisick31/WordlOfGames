package com.example.worldofgames.data

import androidx.room.TypeConverter
import java.util.*

class MyTypeConverter {
    @TypeConverter
    fun fromHobbies(hobbies: List<String>): String {
        return hobbies.joinToString(",")
    }

    @TypeConverter
    fun toHobbies(data: String): List<String> {
        return data.split(",")
    }
}
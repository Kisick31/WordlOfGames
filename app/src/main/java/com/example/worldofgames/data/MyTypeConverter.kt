package com.example.worldofgames.data

import androidx.room.TypeConverter

class MyTypeConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toStringList(string: String): List<String> {
        return string.split(",")
    }
}
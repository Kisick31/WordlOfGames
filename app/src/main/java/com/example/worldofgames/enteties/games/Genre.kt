package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class Genre(

    @SerializedName("id")
    var genreId: Int,
    @SerializedName("name")
    var name: String
)
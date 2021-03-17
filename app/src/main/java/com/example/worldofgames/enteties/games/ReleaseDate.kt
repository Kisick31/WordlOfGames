package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class ReleaseDate(
    @SerializedName("platform")
    var platform: Int,
    @SerializedName("human")
    var human: String,
    @SerializedName("id")
    var id: Int
)
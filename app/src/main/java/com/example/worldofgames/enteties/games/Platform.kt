package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class Platform(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
)
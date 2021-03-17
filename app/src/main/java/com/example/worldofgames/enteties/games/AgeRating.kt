package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class AgeRating(
    @SerializedName("id")
    var id: Int,
    @SerializedName("rating")
    var rating: Int
)
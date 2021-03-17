package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class Cover(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_id")
    var imageId: String
)
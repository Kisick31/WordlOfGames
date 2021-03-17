package com.example.worldofgames.enteties.games


import com.google.gson.annotations.SerializedName

data class Screenshot(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_id")
    var imageId: String
)
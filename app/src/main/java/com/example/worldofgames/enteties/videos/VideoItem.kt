package com.example.worldofgames.enteties.videos

import com.google.gson.annotations.SerializedName

data class VideoItem(
    @SerializedName("name")
    var name: String,
    @SerializedName("video_id")
    var videoUrl: String
    )
package com.example.worldofgames.enteties.games

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.worldofgames.converters.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "games")
@TypeConverters(value = [
    AgeRatingTC::class,
    GenreTC::class,
    PlatformTC::class,
    ReleaseDateTC::class,
    ScreenshotTC::class,
    CoverTC::class
])
open class GameItem(
    @PrimaryKey(autoGenerate = true)
    var uniqueId: Int,
    @SerializedName("age_ratings")
    var ageRatings: List<AgeRating>?,
    @SerializedName("cover")
    var cover: Cover,
    @SerializedName("genres")
    var genres: List<Genre>,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("platforms")
    var platforms: List<Platform>?,
    @SerializedName("rating")
    var rating: Double,
    @SerializedName("release_dates")
    var releaseDates: List<ReleaseDate>,
    @SerializedName("screenshots")
    var screenshots: List<Screenshot>,
    @SerializedName("summary")
    var summary: String
)
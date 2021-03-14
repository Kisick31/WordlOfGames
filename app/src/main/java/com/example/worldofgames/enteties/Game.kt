package com.example.worldofgames.enteties

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Game
@Ignore
constructor(
    var id: Int,
    var name: String,
    var coverUrl: String,
    var ageRating: String,
    var rating: Int,
    var genres: List<String>,
    var releaseDate: String,
    var platforms: List<String>,
    var screenshots: List<String>,
    var summary: String
) {

    @PrimaryKey(autoGenerate = true)
    var uniqueId: Int = 0

    constructor(
        uniqueId: Int,
        id: Int,
        name: String,
        coverUrl: String,
        ageRating: String,
        rating: Int,
        genres: List<String>,
        releaseDate: String,
        platforms: List<String>,
        screenshots: List<String>,
        summary: String
    ) : this(
        id,
        name,
        coverUrl,
        ageRating,
        rating,
        genres,
        releaseDate,
        platforms,
        screenshots,
        summary
    ) {
        this.uniqueId = uniqueId
    }

}
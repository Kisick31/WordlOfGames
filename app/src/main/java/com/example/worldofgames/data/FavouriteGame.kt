package com.example.worldofgames.data

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "favourite_games")
class FavouriteGame : Game {
    constructor(
        uniqueId: Int,
        id: Int,
        name: String,
        developerName: String,
        coverUrl: String,
        ageRating: String,
        rating: Int,
        genres: List<String>,
        releaseDate: String,
        platforms: List<String>,
        screenshots: List<String>,
        summary: String
    ) : super(
        uniqueId, id, name, developerName, coverUrl,
        ageRating, rating, genres, releaseDate,
        platforms, screenshots, summary
    )

    @Ignore
    constructor(game: Game) : super(
        game.uniqueId, game.id, game.name, game.developerName, game.coverUrl, game.ageRating,
        game.rating, game.genres, game.releaseDate, game.platforms, game.screenshots, game.summary
    )
}

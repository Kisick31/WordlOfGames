package com.example.worldofgames.enteties

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "favourite_games")
class FavouriteGame : Game {
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
    ) : super(
        uniqueId, id, name, coverUrl,
        ageRating, rating, genres, releaseDate,
        platforms, screenshots, summary
    )

    @Ignore
    constructor(game: Game) : super(
        game.uniqueId, game.id, game.name, game.coverUrl, game.ageRating,
        game.rating, game.genres, game.releaseDate, game.platforms, game.screenshots, game.summary
    )
}

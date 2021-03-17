package com.example.worldofgames.enteties.hype_games

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
import com.example.worldofgames.converters.*
import com.example.worldofgames.enteties.games.*

@Entity(tableName = "hype_games")
@TypeConverters(value = [
    AgeRatingTC::class,
    GenreTC::class,
    PlatformTC::class,
    ReleaseDateTC::class,
    ScreenshotTC::class,
    CoverTC::class
])
class HypeGameItem : GameItem {
    constructor(
        uniqueId: Int,
        ageRatings: List<AgeRating>,
        cover: Cover,
        genres: List<Genre>,
        id: Int,
        name: String,
        platforms: List<Platform>,
        rating: Double,
        releaseDates: List<ReleaseDate>,
        screenshots: List<Screenshot>,
        summary: String
    ) : super(
        uniqueId, ageRatings, cover, genres,
        id, name, platforms, rating,
        releaseDates, screenshots, summary
    )
    @Ignore
    constructor(game: GameItem) : super(
        game.uniqueId, game.ageRatings, game.cover, game.genres, game.id, game.name,
        game.platforms, game.rating, game.releaseDates, game.screenshots, game.summary
    )
}
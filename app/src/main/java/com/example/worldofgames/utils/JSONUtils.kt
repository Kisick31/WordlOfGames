package com.example.worldofgames.utils

import android.content.Context
import com.example.worldofgames.R
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.Video
import org.json.JSONArray
import org.json.JSONException
import kotlin.collections.*

class JSONUtils{
    companion object {
        fun getGamesFromJSON(jsonArray: JSONArray, context: Context): ArrayList<Game> {

            val games = ArrayList<Game>()

            for (i in 0 until jsonArray.length()) {

                val resultGame = jsonArray.getJSONObject(i)
                val id = resultGame.getInt("id")
                val name = resultGame.getString("name")
                val coverUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big_2x/" +
                        "${resultGame.getJSONObject("cover").getString("image_id")}.jpg"

                val summary = try {
                    resultGame.getString("summary")
                } catch (e: JSONException){
                    ""
                }

                val developerName = try {
                    kotlin.run {
                        var trueDeveloperName = "Developer"
                        for (j in 0 until resultGame.getJSONArray("involved_companies")
                            .length()) {
                            if (resultGame.getJSONArray("involved_companies").getJSONObject(j)
                                    .getBoolean("developer")
                            )
                                trueDeveloperName = resultGame.getJSONArray("involved_companies")
                                    .getJSONObject(j).getJSONObject("company").getString("name")
                        }
                        trueDeveloperName
                    }
                } catch (e: JSONException) {
                    context.getString(R.string.developer_not_specified)
                }

                val ageRating = try {
                    when (resultGame.getJSONArray("age_ratings").getJSONObject(0).getInt("rating")) {
                        1 -> context.getString(R.string.pegi_three)
                        2 -> context.getString(R.string.pegi_seven)
                        3 -> context.getString(R.string.pegi_twelve)
                        4 -> context.getString(R.string.pegi_sixteen)
                        5 -> context.getString(R.string.pegi_eighteen)
                        6 -> context.getString(R.string.rating_exepted)
                        7 -> context.getString(R.string.early_childhood)
                        8 -> context.getString(R.string.everyone)
                        9 -> context.getString(R.string.everyone_10_and_older)
                        10 -> context.getString(R.string.teen)
                        11 -> context.getString(R.string.mature)
                        12 -> context.getString(R.string.adults_only_18)
                        else -> "Age rating unknown"
                    }
                } catch (e: JSONException) {
                    context.getString(R.string.rating_not_specified)
                }
                val rating = try {
                    resultGame.getInt("rating")
                } catch (e: JSONException) {
                    0
                }
                val genres = try {
                    kotlin.run {
                        val genresResult = ArrayList<String>()
                        for (j in 0 until resultGame.getJSONArray("genres").length()) {
                            genresResult.add(
                                resultGame.getJSONArray("genres").getJSONObject(j).getString("name")
                            )
                        }
                        genresResult
                    }
                } catch (e: JSONException) {
                    arrayListOf()
                }
                val screenshots = try {
                    kotlin.run {
                        val screenshotsResult = ArrayList<String>()
                        for (j in 0 until resultGame.getJSONArray("screenshots").length()) {
                            screenshotsResult.add(
                                "https://images.igdb.com/igdb/image/upload/t_720p/${
                                    resultGame.getJSONArray("screenshots")
                                        .getJSONObject(j).getString("image_id")
                                }.jpg"
                            )
                        }
                        screenshotsResult
                    }
                } catch (e: JSONException) {
                    arrayListOf()
                }
                val releaseDate = try {
                    resultGame.getJSONArray("release_dates").getJSONObject(0).getString("human")
                } catch (e: JSONException) {
                    context.getString(R.string.unknown)
                }
                val platforms = try {
                    kotlin.run {
                        val platformsResult = ArrayList<String>()
                        for (j in 0 until resultGame.getJSONArray("platforms").length()) {
                            platformsResult.add(resultGame.getJSONArray("platforms")
                                .getJSONObject(j).getString("name")
                            )
                        }
                        platformsResult
                    }
                } catch (e: JSONException) {
                    arrayListOf()
                }

                val game = Game(
                    id,
                    name,
                    developerName,
                    coverUrl,
                    ageRating,
                    rating,
                    genres,
                    releaseDate,
                    platforms,
                    screenshots,
                    summary
                )
                games.add(game)
            }
            return games
        }

        fun getVideosFromJSON(jsonArray: JSONArray): ArrayList<Video>{
            val videos = ArrayList<Video>()
            for(i in 0 until jsonArray.length()){
                val name = jsonArray.getJSONObject(i).getString("name")
                val videoUrl = "https://www.youtube.com/watch?v=${jsonArray.getJSONObject(i).getString("video_id")}"
                videos.add(Video(name, videoUrl))
            }
            return videos
        }
    }
}
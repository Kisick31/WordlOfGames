package com.example.worldofgames.utils

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class NetworkUtils {

    companion object {

        private const val TOKEN_URL = "https://id.twitch.tv/oauth2/token"

        private const val PARAMS_CLIENT_ID = "client_id"
        private const val PARAMS_CLIENT_SECRET = "client_secret"
        private const val PARAMS_GRANT_TYPE = "grant_type"

        private const val CLIENT_ID = "2va411riu61eplp40btvmb5obk7rzo"
        private const val SECRET_CODE = "0pjmfent15b9sw6ijbnshge6naefos"
        private const val GRANT_TYPE = "client_credentials"

        private val V4_GAMES_URL = URL("https://api.igdb.com/v4/games")
        private val V4_VIDEOS_URL = URL("https://api.igdb.com/v4/game_videos")

        private fun buildTokenURL(): URL {

            val uri: Uri = Uri.parse(TOKEN_URL).buildUpon()
                .appendQueryParameter(PARAMS_CLIENT_ID, CLIENT_ID)
                .appendQueryParameter(PARAMS_CLIENT_SECRET, SECRET_CODE)
                .appendQueryParameter(PARAMS_GRANT_TYPE, GRANT_TYPE).build()

            return URL(uri.toString())
        }

        fun getGamesFromTwitch(): JSONArray {
            var jsonArray = JSONArray()
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val result = async { getGamesFromTwitchJSONArray() }
                    jsonArray = result.await()
                }
                job.join()
            }
            return jsonArray
        }

        private fun getGamesFromTwitchJSONArray(): JSONArray {
            val urlConnection: HttpURLConnection
            val builder = StringBuilder()
            try {
                urlConnection = V4_GAMES_URL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Client-ID", CLIENT_ID)
                urlConnection.setRequestProperty(
                    "Authorization",
                    "Bearer toytxz5vr5ezf3lgrdjvpap5hnrc3e"
                )
                urlConnection.setRequestProperty("Accept", "application/json")

                urlConnection.doOutput = true
                val os: OutputStream = urlConnection.outputStream
                val osw = OutputStreamWriter(os, "UTF-8")
                osw.write(
                    "fields name, " +
                            "cover.image_id, " +
                            "screenshots.image_id, " +
                            "genres.name, " +
                            "age_ratings.rating, " +
                            "rating, " +
                            "involved_companies.company.name, " +
                            "involved_companies.developer, " +
                            "release_dates.human, " +
                            "platforms.name, " +
                            "summary;" +
                            "sort rating desc;" +
                            "where rating != null & rating_count>70 & version_parent = null & parent_game = null;" +
                            "l 100;"
                )
                osw.flush()
                osw.close()

                val inputStream = urlConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } catch (e: IOException) {

            } catch (e: JSONException) {

            }
            return JSONArray(builder.toString())

        }

        fun getVideosOfTheGame(gameId: Int): JSONArray {
            var jsonArray = JSONArray()
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val result = async { getVideosJSONArray(gameId) }
                    jsonArray = result.await()
                }
                job.join()
            }
            return jsonArray
        }

        private fun getVideosJSONArray(gameId: Int): JSONArray {
            val urlConnection: HttpURLConnection
            val builder = StringBuilder()
            try {
                urlConnection = V4_VIDEOS_URL.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Client-ID", CLIENT_ID)
                urlConnection.setRequestProperty(
                    "Authorization",
                    "Bearer toytxz5vr5ezf3lgrdjvpap5hnrc3e"
                )
                urlConnection.setRequestProperty("Accept", "application/json")

                urlConnection.doOutput = true
                val os: OutputStream = urlConnection.outputStream
                val osw = OutputStreamWriter(os, "UTF-8")
                osw.write("fields name, video_id; where game = ${gameId};")
                osw.flush()
                osw.close()

                val inputStream = urlConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } catch (e: IOException) {

            } catch (e: JSONException) {

            }
            return JSONArray(builder.toString())
        }

        fun getTokenFromTwitch(): JSONObject {
            var jsonObject = JSONObject()
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val result = async { getTokenJSONObject() }
                    jsonObject = result.await()
                }
                job.join()
            }
            return jsonObject
        }

        private fun getTokenJSONObject(): JSONObject {
            val urlConnection: HttpURLConnection
            val builder = StringBuilder()
            try {
                urlConnection = buildTokenURL().openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"

                val inputStream = urlConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }

            } catch (e: IOException) {

            } catch (e: JSONException) {

            }
            return JSONObject(builder.toString())
        }

        fun hasConnection(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }
    }
}
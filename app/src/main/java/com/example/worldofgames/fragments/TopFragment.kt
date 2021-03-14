package com.example.worldofgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.MainActivity
import com.example.worldofgames.MainActivity.Companion.gameID
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.enteties.Game
import com.example.worldofgames.data.GameViewModel
import com.example.worldofgames.utils.JSONUtils
import com.example.worldofgames.utils.NetworkUtils

class TopFragment : Fragment(), GamesAdapter.OnCoverClickListener {

    private lateinit var recyclerViewCover: RecyclerView
    private lateinit var viewModel: GameViewModel
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCoverClick(position: Int, game: Game) {
        gameID = game.id
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
        }
    }
    private fun downloadData() {
        val games = JSONUtils.getGamesFromJSON(NetworkUtils.getGamesFromTwitch(), requireActivity())
        if (games.isNotEmpty()) {
            viewModel.deleteAllGames()
            for (game in games) {
                viewModel.insertGame(game)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity()
        val view = inflater.inflate(R.layout.fragment_top, container, false)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
            .create(GameViewModel::class.java)
        recyclerViewCover = view.findViewById(R.id.recyclerViewCover)

        recyclerViewCover.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewCover.adapter = gamesAdapter
        if (NetworkUtils.hasConnection(activity.applicationContext)&&MainActivity.wasLoaded) {
            downloadData()
            MainActivity.wasLoaded = false
        }
        val gamesFromLiveData = viewModel.games
        gamesFromLiveData.observe(this, {
            gamesAdapter.games = it
        })
        return view
    }

}
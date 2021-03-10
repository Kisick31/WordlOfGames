package com.example.worldofgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.MainActivity.Companion.gameID
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.data.Game
import com.example.worldofgames.data.MainViewModel

class FavouriteFragment : Fragment(), GamesAdapter.OnCoverClickListener {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerViewFavouriteGames: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerViewFavouriteGames = view.findViewById(R.id.recyclerViewFavouriteGames)
        recyclerViewFavouriteGames.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewFavouriteGames.adapter = gamesAdapter
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            MainViewModel::class.java)
        val favouriteGames = viewModel.favouriteGames
        favouriteGames.observe(this, {
            gamesAdapter.games = it
        })

        return view
    }

    override fun onCoverClick(position: Int, game: Game) {
        gameID = game.id
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
        }
    }

}
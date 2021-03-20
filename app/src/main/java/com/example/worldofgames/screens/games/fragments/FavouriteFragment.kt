package com.example.worldofgames.screens.games.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.databinding.FragmentFavouriteBinding
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.enteties.games.GameItem

class FavouriteFragment : Fragment(), GamesAdapter.OnCoverClickListener {

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFavouriteBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_favourite, container, false
            )
        binding.recyclerViewFavouriteGames.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        binding.recyclerViewFavouriteGames.adapter = gamesAdapter
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            GameViewModel::class.java)
        val favouriteGames = viewModel.favouriteGames
        favouriteGames.observe(viewLifecycleOwner, {
            gamesAdapter.games = it
        })

        return binding.root
    }

    override fun onCoverClick(position: Int, game: GameItem) {
        gameID = game.id
        view?.let { Navigation.findNavController(it).navigate(R.id.action_favouriteFragment_to_detailFragment) }
    }

}
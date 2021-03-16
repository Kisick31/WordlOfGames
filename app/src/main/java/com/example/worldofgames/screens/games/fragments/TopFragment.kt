package com.example.worldofgames.screens.games.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.screens.games.MainActivity.Companion.wasLoaded
import kotlinx.android.synthetic.main.fragment_top.view.*

class TopFragment : Fragment(), GamesAdapter.OnCoverClickListener {

    private lateinit var recyclerViewCover: RecyclerView
    private lateinit var viewModel: GameViewModel
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCoverClick(position: Int, game: GameItem) {
        gameID = game.id
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
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
        val loading = viewModel.loading
        recyclerViewCover = view.findViewById(R.id.recyclerViewCover)
        recyclerViewCover.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewCover.adapter = gamesAdapter
        recyclerViewCover.itemAnimator?.changeDuration = 0
        val gamesFromLiveData = viewModel.games
        gamesFromLiveData.observe(this, {
            gamesAdapter.games = it
        })
        viewModel.mutableIsDataLoading.observe(this, {
            Log.d("KEK", it.toString())
            view.progressBarLoad.visibility = if(it!=null && it) View.VISIBLE else View.GONE
        })


            viewModel.loadData()
            wasLoaded = true
        view.progressBarLoad.visibility = View.GONE
        return view
    }


}
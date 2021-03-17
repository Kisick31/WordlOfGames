package com.example.worldofgames.screens.games.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.R
import com.example.worldofgames.adapters.OnHypeGamesAdapter
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.screens.games.MainActivity

class SettingsFragment : Fragment() {

    private lateinit var recyclerViewOnHype: RecyclerView
    private lateinit var onHypeGamesAdapter: OnHypeGamesAdapter

    fun onCoverClick(position: Int, game: GameItem) {
        MainActivity.gameID = game.id
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view


    }
    
}
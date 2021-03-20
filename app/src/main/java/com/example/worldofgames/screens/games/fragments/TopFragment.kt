package com.example.worldofgames.screens.games.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.adapters.OnHypeGamesAdapter
import com.example.worldofgames.constants.GameType
import com.example.worldofgames.databinding.FragmentTopBinding
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.enteties.hype_games.HypeGameItem
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.screens.games.MainActivity.Companion.gameType
import java.util.*


class TopFragment : Fragment(), GamesAdapter.OnCoverClickListener,
    OnHypeGamesAdapter.OnCoverClickListener {

    private lateinit var viewModel: GameViewModel

    private lateinit var recyclerViewCover: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter

    private lateinit var recyclerViewOnHype: RecyclerView

    private lateinit var autoScrollLayoutManager: LinearLayoutManager
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var position: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding: FragmentTopBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_top, container, false
            )

        recyclerViewOnHype = binding.recyclerViewOnHype
        recyclerViewCover = binding.recyclerViewCover

        val activity = requireActivity()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
            .create(GameViewModel::class.java)

        recyclerViewCover.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewCover.adapter = gamesAdapter
        recyclerViewCover.itemAnimator?.changeDuration = 0

        viewModel.games.observe(viewLifecycleOwner, {
            gamesAdapter.games = it
        })

        viewModel.isDataLoading.observe(viewLifecycleOwner, {
            binding.progressBarLoad.visibility = if (it != null && it) View.VISIBLE else View.GONE
        })

        viewModel.loadData()
        binding.progressBarLoad.visibility = View.GONE

        autoScrollLayoutManager = object : LinearLayoutManager(context,
            HORIZONTAL, false) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView,
                state: RecyclerView.State,
                position: Int,
            ) {
                val smoothScroller: LinearSmoothScroller =
                    object : LinearSmoothScroller(context) {
                        private val SPEED = 100f
                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                            return SPEED / displayMetrics.densityDpi
                        }
                    }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }
        binding.recyclerViewOnHype.layoutManager = autoScrollLayoutManager
        val onHypeGamesAdapter = OnHypeGamesAdapter(this, viewModel.getOnHypeGames(), requireContext())
        binding.recyclerViewOnHype.adapter = onHypeGamesAdapter

        position = Int.MAX_VALUE / 2
        binding.recyclerViewOnHype.scrollToPosition(position)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewOnHype)
        binding.recyclerViewOnHype.smoothScrollBy(5, 0)

        binding.recyclerViewOnHype.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1) {
                    stopAutoScrollBanner()
                } else if (newState == 0) {
                    position = autoScrollLayoutManager.findFirstCompletelyVisibleItemPosition()
                    runAutoScrollBanner()
                }
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        runAutoScrollBanner()
    }

    override fun onStop() {
        super.onStop()
        stopAutoScrollBanner()
    }

    private fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask!!.cancel()
            timer!!.cancel()
            timer = null
            timerTask = null
            position = autoScrollLayoutManager.findFirstCompletelyVisibleItemPosition()
        }
    }

    private fun runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (position == Int.MAX_VALUE) {
                        position = Int.MAX_VALUE / 2
                        recyclerViewOnHype.scrollToPosition(position)
                        recyclerViewOnHype.smoothScrollBy(5, 0)
                    } else {
                        position++
                        recyclerViewOnHype.smoothScrollToPosition(position)
                    }
                }
            }
            timer!!.schedule(timerTask, 4000, 4000)
        }
    }

    override fun onCoverClick(position: Int, game: GameItem) {
        gameID = game.id
        gameType = GameType.SIMPLE_GAME
        view?.findNavController()?.navigate(R.id.action_topFragment_to_detailFragment)
    }

    override fun onHypeCoverClick(position: Int, game: HypeGameItem) {
        gameID = game.id
        gameType = GameType.HYPE_GAME
        view?.findNavController()?.navigate(R.id.action_topFragment_to_detailFragment)
    }

}
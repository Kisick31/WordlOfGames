package com.example.worldofgames.screens.games.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.worldofgames.R
import com.example.worldofgames.adapters.GamesAdapter
import com.example.worldofgames.adapters.OnHypeGamesAdapter
import com.example.worldofgames.constants.GameType
import com.example.worldofgames.enteties.HypeGame
import com.example.worldofgames.enteties.games.GameItem
import com.example.worldofgames.screens.games.GameViewModel
import com.example.worldofgames.screens.games.MainActivity.Companion.gameID
import com.example.worldofgames.screens.games.MainActivity.Companion.gameType
import kotlinx.android.synthetic.main.fragment_top.view.*
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

    override fun onCoverClick(position: Int, game: GameItem) {
        gameID = game.id
        gameType = GameType.SIMPLE_GAME
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
        }
    }

    override fun onHypeCoverClick(position: Int, game: HypeGame) {
        gameID = game.id
        gameType = GameType.HYPE_GAME
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fl_wrapper, DetailFragment())
            commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        val view = inflater.inflate(R.layout.fragment_top, container, false)

        val activity = requireActivity()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.application)
            .create(GameViewModel::class.java)

        recyclerViewOnHype = view.findViewById(R.id.recyclerViewOnHype)
        recyclerViewCover = view.findViewById(R.id.recyclerViewCover)

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
        recyclerViewOnHype.layoutManager = autoScrollLayoutManager

        val onHypeGamesAdapter = OnHypeGamesAdapter(this, viewModel.loadOnHypeGames(), requireContext())
        recyclerViewOnHype.adapter = onHypeGamesAdapter

        position = Int.MAX_VALUE / 2
        recyclerViewOnHype.scrollToPosition(position)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewOnHype)
        recyclerViewOnHype.smoothScrollBy(5, 0)

        recyclerViewOnHype.addOnScrollListener(object : OnScrollListener() {
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

        recyclerViewCover.layoutManager = GridLayoutManager(context, 2)
        gamesAdapter = GamesAdapter(this)
        recyclerViewCover.adapter = gamesAdapter
        recyclerViewCover.itemAnimator?.changeDuration = 0

        viewModel.games.observe(this, {
            gamesAdapter.games = it
        })

        viewModel.isDataLoading.observe(this, {
            view.progressBarLoad.visibility = if (it != null && it) View.VISIBLE else View.GONE
        })

        viewModel.loadData()
        view.progressBarLoad.visibility = View.GONE

        return view
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
}
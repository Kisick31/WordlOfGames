package com.example.worldofgames.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.R
import com.example.worldofgames.enteties.Game
import com.squareup.picasso.Picasso


class GamesAdapter(private var listener: OnCoverClickListener) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>(){

    var games: List<Game> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.games_item, parent, false)
        return GameViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.itemView.tag = game
        Picasso.get().load(game.coverUrl).into(holder.imageViewSmallCover)
    }

    class GameViewHolder(itemView: View, private val listener: OnCoverClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageViewSmallCover: ImageView = itemView.findViewById(R.id.imageViewSmallCover)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onCoverClick(adapterPosition, itemView.tag as Game)
        }
    }



    interface OnCoverClickListener{
        fun onCoverClick(position: Int, game: Game)
    }
}

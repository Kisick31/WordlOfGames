package com.example.worldofgames.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.R
import com.example.worldofgames.enteties.games.GameItem
import com.squareup.picasso.Picasso


class GamesAdapter(private var listener: OnCoverClickListener) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>(){

    var games: List<GameItem> = listOf()
        set(value) {
            field = value
            notifyItemRangeChanged(field.lastIndex,value.size)
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
        Picasso.get().load("https://images.igdb.com/igdb/image/upload/t_cover_big_2x/" +
                game.cover.imageId + ".jpg").into(holder.imageViewSmallCover)
        holder.textViewGameTitle.text = game.name
    }

    class GameViewHolder(itemView: View, private val listener: OnCoverClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageViewSmallCover: ImageView = itemView.findViewById(R.id.imageViewSmallCover)
        var textViewGameTitle: TextView = itemView.findViewById(R.id.textViewGameTitle)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onCoverClick(adapterPosition, itemView.tag as GameItem)
        }
    }

    interface OnCoverClickListener{
        fun onCoverClick(position: Int, game: GameItem)
    }
}

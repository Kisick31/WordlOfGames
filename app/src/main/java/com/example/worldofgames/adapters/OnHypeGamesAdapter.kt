package com.example.worldofgames.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.R
import com.example.worldofgames.enteties.HypeGame
import com.squareup.picasso.Picasso

class OnHypeGamesAdapter (private var listener: OnCoverClickListener,
                          private var games: List<HypeGame>?,
                          private var context: Context)
    : RecyclerView.Adapter<OnHypeGamesAdapter.GameViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.on_hype_item, parent, false)
        return GameViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return if (games==null) 0 else Integer.MAX_VALUE
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games?.get(position% games!!.size)

        holder.itemView.tag = game
        if (game!=null) {
            Picasso.get().load("https://images.igdb.com/igdb/image/upload/t_cover_big/" +
                    game.cover.imageId + ".jpg").into(holder.imageViewSmallCover)
            holder.textViewReleaseDate.text = game.releaseDates[0].human
            holder.textViewTitle.text = game.name
        }
    }

    class GameViewHolder(itemView: View, private val listener: OnCoverClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageViewSmallCover: ImageView = itemView.findViewById(R.id.imageViewCoverOnHype)
        var textViewTitle: TextView = itemView.findViewById(R.id.textViewHypeGameTitle)
        var textViewReleaseDate: TextView = itemView.findViewById(R.id.textViewHypeGameReleaseDate)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onHypeCoverClick(adapterPosition, itemView.tag as HypeGame)
        }
    }

    interface OnCoverClickListener{
        fun onHypeCoverClick(position: Int, game: HypeGame)
    }
}
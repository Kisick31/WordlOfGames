package com.example.worldofgames.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worldofgames.R
import com.example.worldofgames.enteties.videos.VideoItem

class VideoAdapter(private var listener: OnPlayClickListener) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var videoItems: List<VideoItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoItems[position]
        holder.itemView.tag = "https://www.youtube.com/watch?v=${video.videoUrl}"
        holder.textViewVideoName.text = video.name
    }

    class VideoViewHolder(
        itemView: View,
        private val listener: OnPlayClickListener,
        var textViewVideoName: TextView = itemView.findViewById(R.id.textViewVideoName)
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onPlayClick(itemView.tag as String)
        }
    }

    interface OnPlayClickListener {
        fun onPlayClick(url: String)
    }
}
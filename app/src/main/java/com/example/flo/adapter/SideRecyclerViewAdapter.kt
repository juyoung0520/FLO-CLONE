package com.example.flo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.*
import com.example.flo.databinding.ItemPlayListBinding
import com.example.flo.databinding.ItemSongBinding

class SideRecyclerViewAdapter(): RecyclerView.Adapter<SideRecyclerViewAdapter.SideRecyclerViewHolder>() {
    private val songs = ArrayList<Song>()

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SideRecyclerViewAdapter.SideRecyclerViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SideRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SideRecyclerViewAdapter.SideRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(songs[position], position+1)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SideRecyclerViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song, rank: Int) {
            binding.itemTrackRankExp1Tv.text = rank.toString()
            binding.itemTrackTitleExp1Tv.text = song.title
            binding.itemTrackSingerExp1Tv.text = song.singer
        }
    }
}
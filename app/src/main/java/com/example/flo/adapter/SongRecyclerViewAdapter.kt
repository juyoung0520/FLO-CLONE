package com.example.flo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.databinding.ItemPlayListBinding

class SongRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<SongRecyclerViewAdapter.SongRecyclerViewHolder>() {
    private val songs = ArrayList<Song>()

    interface OnItemClickListener {
        //fun onItemAlbumClick(album: Album)
        fun onItemMoreClick(songId: Int)
    }

    private lateinit var mItemCLickListener: OnItemClickListener

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        mItemCLickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongRecyclerViewAdapter.SongRecyclerViewHolder {
        val binding = ItemPlayListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SongRecyclerViewAdapter.SongRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(songs[position])

//        holder.binding.itemAlbumIv.setOnClickListener {
//            mItemCLickListener.onItemAlbumClick(songs[position].album!!)
//        }

        holder.binding.itemBtnMoreIv.setOnClickListener {
            mItemCLickListener.onItemMoreClick(songs[position].id)
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongRecyclerViewHolder(val binding: ItemPlayListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            if (song.coverImgUrl == "") {
                Glide.with(context).load(song.albumImg!!).into(binding.itemAlbumIv)
            } else {
                Glide.with(context).load(song.coverImgUrl).into(binding.itemAlbumIv)
            }

            binding.itemTitleTv.text = song.title
            binding.itemSingerTv.text = song.singer
        }
    }
}
package com.example.flo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumRecyclerViewHolder>() {
    private val albums = ArrayList<Album>()

    interface OnItemClickListener {
        fun onItemClick(album: Album)
    }

    private lateinit var mItemClickListener: OnItemClickListener

    fun setOnItemClickListener(itemClickListener : OnItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album) {
        albums.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumRecyclerViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRecyclerViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albums[position])
        }
    }

    override fun getItemCount(): Int = albums.size

    inner class AlbumRecyclerViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            if (album.coverImgUrl == "") {
                Glide.with(context).load(album.coverImg!!).into(binding.itemTodayAlbumIv)
            } else {
                Glide.with(context).load(album.coverImgUrl).into(binding.itemTodayAlbumIv)
            }

            binding.itemTodayAlbumTitleTv.text = album.name
            binding.itemTodayAlbumSingerTv.text = album.artist
        }
    }

}
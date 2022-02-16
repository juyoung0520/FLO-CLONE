package com.example.flo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.databinding.ItemAlbumListBinding
import com.example.flo.databinding.ItemPlayListBinding

class AlbumListRecyclerViewAdapter(): RecyclerView.Adapter<AlbumListRecyclerViewAdapter.AlbumRecyclerViewHolder>() {
    private val albums = ArrayList<Album>()

    interface OnItemClickListener {
        //fun onItemAlbumClick(album: Album)
        fun onItemMoreClick(albumId: Int)
    }

    private lateinit var mItemCLickListener: OnItemClickListener

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        mItemCLickListener = itemClickListener
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumListRecyclerViewAdapter.AlbumRecyclerViewHolder {
        val binding = ItemAlbumListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AlbumListRecyclerViewAdapter.AlbumRecyclerViewHolder,
        position: Int
    ) {
        holder.bind(albums[position])

//        holder.binding.itemAlbumIv.setOnClickListener {
//            mItemCLickListener.onItemAlbumClick(songs[position].album!!)
//        }

        holder.binding.itemBtnMoreIv.setOnClickListener {
            mItemCLickListener.onItemMoreClick(albums[position].id)
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    inner class AlbumRecyclerViewHolder(val binding: ItemAlbumListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemTitleTv.text = album.name
            binding.itemSingerTv.text = album.artist
            binding.itemInfoTv.text = "${album.releaseDate} | ${album.type} | ${album.category}"
            binding.itemAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}
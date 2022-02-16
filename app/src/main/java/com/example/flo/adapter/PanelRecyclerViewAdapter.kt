package com.example.flo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.databinding.ItemPanelBinding

class PanelRecyclerViewAdapter(val pages: Int): RecyclerView.Adapter<PanelRecyclerViewAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var panelBinding: ItemPanelBinding = ItemPanelBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_panel, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val binding = holder.panelBinding

        when (position % 3) {
            0 -> binding.panelBackgroundIv.setImageResource(R.drawable.img_panel_exp3)
            1 -> binding.panelBackgroundIv.setImageResource(R.drawable.img_panel_exp1)
            else -> binding.panelBackgroundIv.setImageResource(R.drawable.img_panel_exp)
        }

    }

    override fun getItemCount(): Int {
        return pages
    }

}
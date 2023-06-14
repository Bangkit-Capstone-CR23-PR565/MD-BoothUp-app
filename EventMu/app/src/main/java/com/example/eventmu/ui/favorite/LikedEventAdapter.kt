package com.example.eventmu.ui.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventmu.data.local.entity.LikedEventEntity
import com.example.eventmu.databinding.LayoutEventBinding
import com.example.eventmu.ui.detail.DetailActivity

class LikedEventAdapter(private val context: Context, private val list: List<LikedEventEntity>) :
    RecyclerView.Adapter<LikedEventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = list[position]

        Glide.with(holder.itemView.context)
            .load(event.photoUrl)
            .into(holder.binding.ivStoryImage)

        holder.binding.tvEventName.text = event.name
        holder.binding.tvLocation.text = event.location
        holder.binding.tvEventPrice.text = event.pricePerStand

        holder.binding.layoutEvent.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.NAME_EXTRA, event.name)
            intentDetail.putExtra(DetailActivity.DESCRIPTION_EXTRA, event.description)
            intentDetail.putExtra(DetailActivity.PRICE_EXTRA, event.pricePerStand)
            intentDetail.putExtra(DetailActivity.IMAGE_URL_EXTRA, event.photoUrl)
            intentDetail.putExtra(DetailActivity.LOCATION_EXTRA, event.location)
            intentDetail.putExtra(DetailActivity.CATEGORY_EXTRA, event.category)
            intentDetail.putExtra(DetailActivity.ID_EXTRA, event.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    inner class ViewHolder(val binding: LayoutEventBinding) : RecyclerView.ViewHolder(binding.root)
}
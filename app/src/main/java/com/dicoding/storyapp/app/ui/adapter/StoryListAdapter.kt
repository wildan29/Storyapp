package com.dicoding.storyapp.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.models.StoryRoomDataModel
import com.dicoding.storyapp.databinding.ListStoryBinding

class StoryListAdapter(private val onClick: (String?) -> Unit) :
    PagingDataAdapter<StoryRoomDataModel, StoryListAdapter.StoryViewHolder>(StoryDiffCallback) {

    class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryRoomDataModel, onClick: (String?) -> Unit) {
            binding.apply {
                card.setOnClickListener {
                    onClick(item.id)
                }
                titleStory.text = item.name
                Glide.with(root.context)
                    .load(item.photoUrl)
                    .into(imageView3)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user, onClick)
            holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.scale
                )
            )
        }
    }
}

object StoryDiffCallback : DiffUtil.ItemCallback<StoryRoomDataModel>() {


    override fun areContentsTheSame(
        oldItem: StoryRoomDataModel,
        newItem: StoryRoomDataModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(
        oldItem: StoryRoomDataModel,
        newItem: StoryRoomDataModel
    ): Boolean {
        return oldItem == newItem
    }
}
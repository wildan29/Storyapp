package com.dicoding.storyapp.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.models.ListStoryItem
import com.dicoding.storyapp.databinding.ListStoryBinding

class StoryListAdapter(private val onClick: (String?) -> Unit) :
    ListAdapter<ListStoryItem, StoryListAdapter.StoryViewHolder>(StoryDiffCallback) {

    class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem, onClick: (String?) -> Unit) {
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

object StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
    override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem == newItem
    }
}
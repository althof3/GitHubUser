package com.altop.githubuserapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.databinding.ItemGithubUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListUserAdapter(private val listUser: List<GithubUser>) :
  RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
  
  private lateinit var onItemClickCallback: OnItemClickCallback
  
  interface OnItemClickCallback {
    fun onItemClicked(data: GithubUser)
  }
  
  fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
    this.onItemClickCallback = onItemClickCallback
  }
  
  class ListViewHolder(var binding: ItemGithubUserBinding) : RecyclerView.ViewHolder(binding.root)
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
    val binding = ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ListViewHolder(binding)
  }
  
  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    val user = listUser[position]
    
    Glide.with(holder.itemView.context).load(user.avatarUrl).circleCrop()
      .transition(DrawableTransitionOptions.withCrossFade()).into(holder.binding.avatar)
    
    holder.binding.itemUsername.text = user.login
    
    holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
  }
  
  override fun getItemCount(): Int = listUser.size
  
}
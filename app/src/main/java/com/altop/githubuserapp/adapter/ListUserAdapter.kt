package com.altop.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altop.githubuserapp.databinding.ItemGithubUserBinding
import com.altop.githubuserapp.model.User
import com.bumptech.glide.Glide

class ListUserAdapter(private val listUser: ArrayList<User>) :
  RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
  
  private lateinit var onItemClickCallback: OnItemClickCallback
  
  interface OnItemClickCallback {
    fun onItemClicked(data: User)
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
    
    Glide.with(holder.itemView.context).load(user.avatar).circleCrop().into(holder.binding.avatar)
    
    "${user.name} (${user.username})".also { holder.binding.itemUsername.text = it }
    holder.binding.itemCompany.text = user.company
    holder.binding.itemLocation.text = user.location
    
    holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
  }
  
  override fun getItemCount(): Int = listUser.size
}
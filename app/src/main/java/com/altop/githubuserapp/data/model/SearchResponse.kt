package com.altop.githubuserapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
  
  @field:SerializedName("items") val items: List<GithubUser>,
)

data class GithubUser(
  
  @field:SerializedName("login") val login: String,
  
  @field:SerializedName("avatar_url") val avatarUrl: String,
)

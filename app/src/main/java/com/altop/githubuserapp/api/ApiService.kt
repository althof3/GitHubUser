package com.altop.githubuserapp.api

import com.altop.githubuserapp.data.model.DetailUserResponse
import com.altop.githubuserapp.data.model.GithubUser
import com.altop.githubuserapp.data.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
  @GET("search/users")
  fun getSearchUsers(
    @Query("q") query: String
  ): Call<SearchResponse>
  
  @GET("users/{username}")
  fun getUserDetail(
    @Path("username") username: String
  ): Call<DetailUserResponse>
  
  @GET("users/{username}/followers")
  fun getUserFollowers(
    @Path("username") username: String
  ): Call<List<GithubUser>>
  
  @GET("users/{username}/following")
  fun getUserFollowing(
    @Path("username") username: String
  ): Call<List<GithubUser>>
}
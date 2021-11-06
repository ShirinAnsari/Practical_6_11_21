package com.example.shirinansaripracticalapp.webservice

import com.example.shirinansaripracticalapp.model.UserResponse
import retrofit2.http.GET

interface ApiService {

    @GET("?results=100")
    suspend fun getUsers(): UserResponse
}
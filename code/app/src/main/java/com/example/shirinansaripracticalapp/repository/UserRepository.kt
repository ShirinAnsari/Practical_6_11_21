package com.example.shirinansaripracticalapp.repository

import com.example.shirinansaripracticalapp.webservice.APIClient
import com.example.shirinansaripracticalapp.webservice.ApiService

object UserRepository {
    private val apiService: ApiService = APIClient.apiClient().create(ApiService::class.java)

    suspend fun getUsers() = apiService.getUsers()
}

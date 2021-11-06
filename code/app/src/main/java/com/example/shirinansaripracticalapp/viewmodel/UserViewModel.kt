package com.example.shirinansaripracticalapp.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shirinansaripracticalapp.R
import com.example.shirinansaripracticalapp.model.UserResponse
import com.example.shirinansaripracticalapp.repository.UserRepository
import com.example.shirinansaripracticalapp.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : BaseViewModel() {

    var userResponse = MutableLiveData<UserResponse>()

    fun getUsers(context: Activity) {
        if (NetworkUtil.isNetworkConnected(context)) {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = UserRepository.getUsers()
                    context.runOnUiThread {
                        userResponse.value = response
                        isLoading.value = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            context.runOnUiThread {
                showMsg.value = context.getString(R.string.internet_error)
            }
        }
    }
}
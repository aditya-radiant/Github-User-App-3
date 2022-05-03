package com.dicoding.picodiploma.submission2.ui.detail


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission2.data.model.Item
import com.dicoding.picodiploma.submission2.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {
    companion object{
        private const val TAG = "FollowersViewModel"

    }

    val userFollowers = MutableLiveData<ArrayList<Item>>()

    fun setUserFollowing(login: String){
        val client = ApiConfig.userService.getUserGithubFollowing(login)
        client.enqueue(object : Callback<ArrayList<Item>> {
            override fun onResponse(
                call: Call<ArrayList<Item>>,
                response: Response<ArrayList<Item>>
            ) {
                if (response.isSuccessful){
                    userFollowers.postValue(response.body())
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowing() : LiveData<ArrayList<Item>>{
        return userFollowers
    }
}
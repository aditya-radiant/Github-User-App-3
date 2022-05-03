package com.dicoding.picodiploma.submission2.ui

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.picodiploma.submission2.data.helper.SettingPreferences
import com.dicoding.picodiploma.submission2.data.model.Item
import com.dicoding.picodiploma.submission2.data.model.UserResponse
import com.dicoding.picodiploma.submission2.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _listItem = MutableLiveData<ArrayList<Item>>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun setSearchUsername(query: String){
        _isLoading.value = true
        val client = ApiConfig.userService.getUserGithub(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listItem.value = response.body()?.items
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun getUsername(_isTrue: Boolean){
        if(_listItem.value == null || _isTrue){
            _isLoading.value = true
            val client = ApiConfig.userService.getUserGithubList()
            client.enqueue(object : Callback<ArrayList<Item>> {
                override fun onResponse(
                    call: Call<ArrayList<Item>>,
                    response: Response<ArrayList<Item>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _listItem.postValue(response.body())

                    }else{
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
        }


    }
    fun getUser(): LiveData<ArrayList<Item>>{
        return _listItem
    }



}
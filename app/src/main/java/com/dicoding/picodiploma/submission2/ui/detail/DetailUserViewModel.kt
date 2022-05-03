package com.dicoding.picodiploma.submission2.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission2.data.model.DetailUser
import com.dicoding.picodiploma.submission2.data.remote.ApiConfig
import com.dicoding.picodiploma.submission2.data.reposository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailUserViewModel(application: Application): ViewModel() {
    val user = MutableLiveData<DetailUser>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val mUserRepository: UserRepository = UserRepository(application)

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun setUserDetail(login: String) {
        _isLoading.value = true
        val client = ApiConfig.userService.getUserGithubDetail(login)
        client.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    user.postValue(response.body())
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun getDetail(): LiveData<DetailUser>{
        return user
    }

    fun insertFavorite(id: Int, avatar_url: String?, login: String?){
        mUserRepository.insertFavoriteUser(id, avatar_url, login)
    }

    suspend fun checkFavorite(id: Int) = mUserRepository.getFavoriteUserById(id)

    fun deleteFavorite(id: Int){
        mUserRepository.deleteFavoriteUser(id)
    }

}
package com.dicoding.picodiploma.submission2.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission2.adapter.UserAdapter
import com.dicoding.picodiploma.submission2.data.datasource.UserEntity
import com.dicoding.picodiploma.submission2.data.helper.FavoriteViewModelFactory
import com.dicoding.picodiploma.submission2.data.model.Item
import com.dicoding.picodiploma.submission2.databinding.ActivityFavoriteUserBinding
import com.dicoding.picodiploma.submission2.ui.detail.DetailUserActivity


class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: FavoriteUserViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()

        viewModel = obtainViewModel(this@FavoriteUserActivity)

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Item) {
                Intent(this@FavoriteUserActivity, DetailUserActivity::class.java).also{
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatar_url)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvGithubUser.layoutManager =LinearLayoutManager(this@FavoriteUserActivity)
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.adapter = userAdapter
        }

        showLoading(true)
        viewModel.getUser().observe(this, {
            if (it != null) {
                val list = convertList(it)
                userAdapter.setData(list)

            }
            showLoading(false)
        })


    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    private fun convertList(users: List<UserEntity>): ArrayList<Item>{
        val listUsers = ArrayList<Item>()
        for (user in users){
            val userMapped = Item(
                user.id,
                user.avatar_url,
                user.login
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}
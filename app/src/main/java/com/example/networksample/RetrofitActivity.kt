package com.example.networksample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networksample.databinding.ActivityRetrofitBinding
import com.example.networksample.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class RetrofitActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRetrofitBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter =CustomRecyclerAdapter()
        binding.cRecyclerView.adapter = adapter
        binding.cRecyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create()).build()

        binding.buttonRequest.setOnClickListener {
            val userService = retrofit.create(UserService::class.java)
            userService.users().enqueue(object : Callback<Repository> {
                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    adapter.userList = response.body() as Repository
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Repository>, t: Throwable) {

                }

            })
        }

    }

    interface UserService {
        @GET("users/devjinjin/repos")
        fun users(): Call<Repository>
    }
}
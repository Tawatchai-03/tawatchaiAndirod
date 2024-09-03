package com.example.homeappdetail

import ApiService
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HouseDetailsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HouseAdapter
    private lateinit var btnBackToAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        recyclerView = findViewById(R.id.recycler_view)
        btnBackToAdd = findViewById(R.id.btn_back_to_add)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HouseAdapter(listOf())
        recyclerView.adapter = adapter

        fetchHouses()

        btnBackToAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchHouses() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getHouses().enqueue(object : Callback<List<House>> {
            override fun onResponse(call: Call<List<House>>, response: Response<List<House>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateHouses(it)
                    }
                } else {
                    Toast.makeText(this@HouseDetailsActivity, "Failed to retrieve houses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<House>>, t: Throwable) {
                Toast.makeText(this@HouseDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

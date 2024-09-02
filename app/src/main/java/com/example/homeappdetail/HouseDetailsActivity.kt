package com.example.homeappdetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class HouseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_details)

        // Base URL of the server
        val baseUrl = "http://10.13.2.144:3000" // Replace with your actual server IP

        // Retrieve data from Intent
        val houseSize = intent.getStringExtra("house_size") ?: "Not available"
        val houseBedrooms = intent.getStringExtra("house_bedrooms") ?: "Not available"
        val houseBathrooms = intent.getStringExtra("house_bathrooms") ?: "Not available"
        val housePrice = intent.getStringExtra("house_price") ?: "Not available"
        val houseCondition = intent.getStringExtra("house_condition") ?: "Not available"
        val houseType = intent.getStringExtra("house_type") ?: "Not available"
        val yearBuilt = intent.getStringExtra("year_built") ?: "Not available"
        val parkingSpaces = intent.getStringExtra("parking_spaces") ?: "Not available"
        val address = intent.getStringExtra("house_address") ?: "Not available"
        val imageUrl = intent.getStringExtra("image_url") ?: ""

        // Bind data to the UI elements
        findViewById<TextView>(R.id.tv_size).text = "ขนาดพื้นที่: $houseSize ตร.ม."
        findViewById<TextView>(R.id.tv_bedrooms).text = "จำนวนห้องนอน: $houseBedrooms"
        findViewById<TextView>(R.id.tv_bathrooms).text = "จำนวนห้องน้ำ: $houseBathrooms"
        findViewById<TextView>(R.id.tv_price).text = "ราคา: $housePrice บาท"
        findViewById<TextView>(R.id.tv_house_condition).text = "สภาพของบ้าน: $houseCondition"
        findViewById<TextView>(R.id.tv_house_type).text = "ประเภทของบ้าน: $houseType"
        findViewById<TextView>(R.id.tv_year_built).text = "ปีที่สร้าง: $yearBuilt"
        findViewById<TextView>(R.id.tv_parking_spaces).text = "จำนวนที่จอดรถ: $parkingSpaces"
        findViewById<TextView>(R.id.tv_address).text = "ที่อยู่: $address"

        // Load the image using Glide
        val imageView = findViewById<ImageView>(R.id.iv_house_image)
        val fullImageUrl = if (imageUrl.isNotEmpty()) "$baseUrl$imageUrl" else ""
        Glide.with(this)
            .load(fullImageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Add a placeholder image
            .error(R.drawable.ic_launcher_background) // Add an error image
            .into(imageView)
    }
}

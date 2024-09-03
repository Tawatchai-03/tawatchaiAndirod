package com.example.homeappdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HouseAdapter(private var houses: List<House>) : RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    inner class HouseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSize: TextView = view.findViewById(R.id.tv_size)
        val tvBedrooms: TextView = view.findViewById(R.id.tv_bedrooms)
        val tvBathrooms: TextView = view.findViewById(R.id.tv_bathrooms)
        val tvPrice: TextView = view.findViewById(R.id.tv_price)
        val tvHouseCondition: TextView = view.findViewById(R.id.tv_house_condition)
        val tvHouseType: TextView = view.findViewById(R.id.tv_house_type)
        val tvYearBuilt: TextView = view.findViewById(R.id.tv_year_built)
        val tvParkingSpaces: TextView = view.findViewById(R.id.tv_parking_spaces)
        val tvAddress: TextView = view.findViewById(R.id.tv_address)
        val ivHouseImage: ImageView = view.findViewById(R.id.iv_house_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_house, parent, false)
        return HouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = houses[position]
        holder.tvSize.text = "ขนาดพื้นที่: ${house.size} ตร.ม."
        holder.tvBedrooms.text = "จำนวนห้องนอน: ${house.bedrooms}"
        holder.tvBathrooms.text = "จำนวนห้องน้ำ: ${house.bathrooms}"
        holder.tvPrice.text = "ราคา: ${house.price} บาท"
        holder.tvHouseCondition.text = "สภาพของบ้าน: ${house.house_condition}"
        holder.tvHouseType.text = "ประเภทของบ้าน: ${house.house_type}"
        holder.tvYearBuilt.text = "ปีที่สร้าง: ${house.year_built}"
        holder.tvParkingSpaces.text = "จำนวนที่จอดรถ: ${house.parking_spaces}"
        holder.tvAddress.text = "ที่อยู่: ${house.address}"

        Glide.with(holder.itemView.context)
            .load("http://10.13.2.144:3000${house.image_url}")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.ivHouseImage)
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    fun updateHouses(newHouses: List<House>) {
        houses = newHouses
        notifyDataSetChanged()
    }
}

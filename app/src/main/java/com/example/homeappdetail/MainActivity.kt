package com.example.homeappdetail

import ApiService
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private var selectedImageUri: Uri? = null
    private var imageFile: File? = null // Declare imageFile here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        val etSize = findViewById<EditText>(R.id.et_size)
        val etBedrooms = findViewById<EditText>(R.id.et_bedrooms)
        val etBathrooms = findViewById<EditText>(R.id.et_bathrooms)
        val etPrice = findViewById<EditText>(R.id.et_price)
        val etHouseCondition = findViewById<EditText>(R.id.et_house_condition)
        val etHouseType = findViewById<EditText>(R.id.et_house_type)
        val etYearBuilt = findViewById<EditText>(R.id.et_year_built)
        val etParkingSpaces = findViewById<EditText>(R.id.et_parking_spaces)
        val etAddress = findViewById<EditText>(R.id.et_address)
        val ivHouseImage = findViewById<ImageView>(R.id.iv_house_image)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnCheckHouseInfo = findViewById<Button>(R.id.btn_check_house_info)
        val btnSelectImage = findViewById<Button>(R.id.btn_select_image)

        // Image selection
        val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    Glide.with(this).load(uri).into(ivHouseImage)
                    imageFile = createTempFileFromUri(uri) // Initialize imageFile
                }
            }
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            selectImageLauncher.launch(intent)
        }

        // Save data
        btnSave.setOnClickListener {
            val size = etSize.text.toString().toDoubleOrNull() ?: 0.0
            val bedrooms = etBedrooms.text.toString().toIntOrNull() ?: 0
            val bathrooms = etBathrooms.text.toString().toIntOrNull() ?: 0
            val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val houseCondition = etHouseCondition.text.toString()
            val houseType = etHouseType.text.toString()
            val yearBuilt = etYearBuilt.text.toString().toIntOrNull() ?: 0
            val parkingSpaces = etParkingSpaces.text.toString().toIntOrNull() ?: 0
            val address = etAddress.text.toString()

            if (imageFile != null) { // Ensure imageFile is not null
                val imagePart = MultipartBody.Part.createFormData(
                    "image", imageFile!!.name,
                    imageFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                )

                val sizePart = RequestBody.create("text/plain".toMediaTypeOrNull(), size.toString())
                val bedroomsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), bedrooms.toString())
                val bathroomsPart = RequestBody.create("text/plain".toMediaTypeOrNull(), bathrooms.toString())
                val pricePart = RequestBody.create("text/plain".toMediaTypeOrNull(), price.toString())
                val houseConditionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), houseCondition)
                val houseTypePart = RequestBody.create("text/plain".toMediaTypeOrNull(), houseType)
                val yearBuiltPart = RequestBody.create("text/plain".toMediaTypeOrNull(), yearBuilt.toString())
                val parkingSpacesPart = RequestBody.create("text/plain".toMediaTypeOrNull(), parkingSpaces.toString())
                val addressPart = RequestBody.create("text/plain".toMediaTypeOrNull(), address)

                apiService.addHouse(
                    sizePart, bedroomsPart, bathroomsPart, pricePart, houseConditionPart,
                    houseTypePart, yearBuiltPart, parkingSpacesPart, addressPart, imagePart
                ).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Data Saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to save data with code: ${response.code()} message", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Failed to save data with error: ${t.message} ", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@MainActivity, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        // Show house info
        btnCheckHouseInfo.setOnClickListener {
            val intent = Intent(this, HouseDetailsActivity::class.java)
            intent.putExtra("house_size", etSize.text.toString())
            intent.putExtra("house_bedrooms", etBedrooms.text.toString())
            intent.putExtra("house_bathrooms", etBathrooms.text.toString())
            intent.putExtra("house_price", etPrice.text.toString())
            intent.putExtra("house_condition", etHouseCondition.text.toString())
            intent.putExtra("house_type", etHouseType.text.toString())
            intent.putExtra("year_built", etYearBuilt.text.toString())
            intent.putExtra("parking_spaces", etParkingSpaces.text.toString())
            intent.putExtra("house_address", etAddress.text.toString())
            intent.putExtra("image_url", "http://<your-server-url>/uploads/${imageFile?.name}") // Replace <your-server-url> with your actual server URL
            startActivity(intent)
        }
    }

    private fun createTempFileFromUri(uri: Uri): File {
        val fileName = getFileName(uri)
        val tempFile = File.createTempFile("temp_image", fileName, cacheDir)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return tempFile
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "unknown_file"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow("_display_name"))
            }
        }
        return fileName
    }
}

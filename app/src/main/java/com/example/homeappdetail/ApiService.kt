import com.example.homeappdetail.House
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/houses")
    fun addHouse(
        @Part("size") size: RequestBody,
        @Part("bedrooms") bedrooms: RequestBody,
        @Part("bathrooms") bathrooms: RequestBody,
        @Part("price") price: RequestBody,
        @Part("house_condition") houseCondition: RequestBody,
        @Part("house_type") houseType: RequestBody,
        @Part("year_built") yearBuilt: RequestBody,
        @Part("parking_spaces") parkingSpaces: RequestBody,
        @Part("address") address: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Void>

    @GET("/houses")
    fun getHouses(): Call<List<House>>
}

package project.nasapictureoftheday
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String, @Query("date") date: String): PictureOfTheDay

    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

class ApiServiceImpl(): ApiService {
    override suspend fun getPictureOfTheDay(apiKey: String, date: String): PictureOfTheDay {
        return ApiService.create().getPictureOfTheDay(apiKey, date)
    }
}

package fi.monami.finnish_parliament_app.network
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
        "https://users.metropolia.fi/~peterh/"


// ignore picture key from url source
val json = Json { ignoreUnknownKeys = true }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

val retrofitService: ParliamentApiService = retrofit.create(ParliamentApiService:: class.java)

interface ParliamentApiService{
    @GET("mps.json")
    suspend fun getParliamentMembers(): List<ParliamentMember>
}
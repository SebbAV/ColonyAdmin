package mx.com.colonyadmin.colonyadmin.Services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface LoginService {


    @POST("user/login")
    fun doLogin(@Body  body: UserRequest): Call<UserResponse>

    @POST("user")
    fun createUser(@Body body: newUser): Call<UserResponse>

    @POST("user/forgot")
    fun sendEmailForgotPassword(@Body body: EmailRequestObject): Call<EmailResposeObject>

    @GET("user/check_code/{CODE}")
    fun sendCodeConfirmation(@Path("CODE")code: String): Call<CodeResponseObject>

    @PUT("user/password_reset")
    fun changePassword(@Body code: UserRequest): Call<PasswordChangedResponse>

    @GET("address/")
    fun getAddress(): Call<AddressResponse>

    @GET("user/get_visitors/{ID}")
    fun getGuestsById(@Path("ID")id: String): Call<GuestListResponse>
}


object RetrofitClient {

    var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null) {
            //TODO While release in Google Play Change the Level to NONE
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .build()

            retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit

    }
}
package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("_id")
        val id: String,
        val email: String,
        val password: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String,
        val address: String,
        @SerializedName("address_number")
        val addressNumber: String,
        val phone: String,
        val role: String,
        val vehicle: String
)
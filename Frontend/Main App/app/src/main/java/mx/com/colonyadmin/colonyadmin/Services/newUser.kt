package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class newUser(
        val email: String,
        val password: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String,
        @SerializedName("object_address_uid")
        val address: String,
        @SerializedName("address_number")
        val addressNumber: String,
        val phone: String,
        val role: String,
        val vehicle: String
)
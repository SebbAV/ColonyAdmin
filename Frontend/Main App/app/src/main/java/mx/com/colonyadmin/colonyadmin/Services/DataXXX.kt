package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class DataXXX(
        @SerializedName("_id")
        val id: String,
        val email: String,
        val code: String
)
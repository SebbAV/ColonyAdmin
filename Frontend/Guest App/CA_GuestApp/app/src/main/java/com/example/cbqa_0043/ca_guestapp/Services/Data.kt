package com.example.cbqa_0043.ca_guestapp.Services

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
    val phone: String,
    val role: String,
    val vehicle: String
)
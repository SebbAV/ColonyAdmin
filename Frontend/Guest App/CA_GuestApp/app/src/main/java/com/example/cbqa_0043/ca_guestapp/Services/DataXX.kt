package com.example.cbqa_0043.ca_guestapp.Services

import com.google.gson.annotations.SerializedName

data class DataXX(
    @SerializedName("_id")
    val id: String,
    @SerializedName("visitor_id")
    val visitorId: String,
    @SerializedName("neighbor_id")
    val neighborId: String,
    @SerializedName("invitation_code")
    val invitationCode: String
)
package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class InvitationGuestRequest(
        @SerializedName("visitor_id")
        val visitorId: String,
        @SerializedName("neighbor_id")
        val neighborId: String
)
package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class DataXXXXXX(
        @SerializedName("_id")
        val id: String,
        val name: String,
        val address: String,
        @SerializedName("entrance_date")
        val entranceDate: String,
        @SerializedName("exit_date")
        val exitDate: String,
        val vehicle: String,
        @SerializedName("object_neighbor_uid")
        val objectNeighborUid: String,
        @SerializedName("object_visitor_uid")
        val objectVisitorUid: String
)
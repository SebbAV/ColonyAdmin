package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class DataXXXXXXX(
        val n: Int,
        val opTime: OpTimeX,
        val electionId: String,
        val ok: Int,
        val operationTime: String,
        @SerializedName("\$clusterTime")
        val clusterTime: ClusterTimeX
)
package mx.com.colonyadmin.colonyadmin.Services

import com.google.gson.annotations.SerializedName

data class DataXXXX(
        val n: Int,
        val nModified: Int,
        val opTime: OpTime,
        val electionId: String,
        val ok: Int,
        val operationTime: String,
        @SerializedName("clusterTime")
        val clusterTime: ClusterTime
)
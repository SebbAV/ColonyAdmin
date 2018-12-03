package mx.com.colonyadmin.colonyadmin.Services

data class DataX(
        val accepted: List<String>,
        val rejected: List<Any>,
        val envelopeTime: Int,
        val messageTime: Int,
        val messageSize: Int,
        val response: String,
        val envelope: Envelope,
        val messageId: String
)
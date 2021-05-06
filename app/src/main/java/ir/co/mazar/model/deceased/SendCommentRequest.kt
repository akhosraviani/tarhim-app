package ir.co.mazar.model.deceased

data class SendCommentRequest(
    val deceasedId: Int,
    val message: String,
    val time: Int
)
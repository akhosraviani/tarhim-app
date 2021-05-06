package ir.co.mazar.model.deceased

data class DeleteCommentRequestModel(
    val commentId: Int,
    val deceasedId: Int
)
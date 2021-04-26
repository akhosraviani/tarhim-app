package ir.co.tarhim.model.deceased

data class DeleteCommentRequestModel(
    val commentId: Int,
    val deceasedId: Int
)
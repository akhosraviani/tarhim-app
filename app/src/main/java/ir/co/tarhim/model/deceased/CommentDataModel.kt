package ir.co.tarhim.model.deceased

data class CommentDataModel(
    val favourite: Boolean,
    val id: Int,
    val imageurl: String,
    val message: String,
    val name: String,
    val reply: String?

)
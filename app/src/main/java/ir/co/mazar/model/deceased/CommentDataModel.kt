package ir.co.mazar.model.deceased

data class CommentDataModel(
    val favourite: Boolean,
    val id: Int,
    val imageurl: String,
    val message: String,
    val likes:Int,
    val name: String,
    val reply: String?

)
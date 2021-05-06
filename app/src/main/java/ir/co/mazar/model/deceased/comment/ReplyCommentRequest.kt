package ir.co.mazar.model.deceased.comment


data class ReplyCommentRequest (
    val commentId : Int ,
    val deceasedId : Int ,
    val txt : String

)
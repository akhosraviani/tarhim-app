package ir.co.mazar.model.deceased.like

data class LikeCommentDataModel (
    val message : String ,
    val code : Int ,
    val userId : Int ,
    val likeCount : Int ,
    val liked : Boolean ,
)

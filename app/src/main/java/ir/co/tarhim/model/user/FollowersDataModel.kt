package ir.co.tarhim.model.user

data class FollowersDataModel(
    val block: Boolean,
    val id: Int,
    val imageurl: String,
    val mobile: String,
    val name: String
)
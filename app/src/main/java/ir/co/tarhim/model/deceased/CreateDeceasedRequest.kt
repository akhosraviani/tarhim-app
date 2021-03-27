package ir.co.tarhim.model.deceased

data class CreateDeceasedRequest(
    val accesstype: String,
    val birthday: String,
    val deathday: String,
    val deathloc: String,
    val description: String,
    val imageurl: String,
    val latitude: Long,
    val longtiude: Long,
    val name: String
)
package ir.co.mazar.model.deceased

data class CreateDeceasedRequest(
    val accesstype: String,
    val birthday: String,
    val deathday: String,
    val deathloc: String,
    val description: String,
    val imageurl: String,
    val latitude: Double,
    val longitude: Double,
    val name: String

)
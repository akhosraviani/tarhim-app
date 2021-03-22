package ir.co.tarhim.model.deceased

data class DeceasedProfileDataModel(
    val birthday: Long,
    val deathday: Long,
    val deathloc: String,
    val description: String,
    val imageurl: String,
    val name: String
)
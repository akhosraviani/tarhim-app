package ir.co.tarhim.model.deceased

data class DeceasedDataModel(
    val birthday: Int,
    val deathday: Int,
    val deathloc: String,
    val description: String,
    val imageurl: String,
    val name: String
)
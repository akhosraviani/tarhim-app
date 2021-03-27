package ir.co.tarhim.model.deceased

data class DeceasedDataModel(
    val birthday: String,
    val deathday: String,
    val id: Int,
    val imageurl: String,
    val name: String,
    val timestamp: Long
)
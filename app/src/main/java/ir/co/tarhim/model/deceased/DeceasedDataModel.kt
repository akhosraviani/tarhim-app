package ir.co.tarhim.model.deceased

data class DeceasedDataModel(
    val birthday: Long,
    val deathday: Long,
    val id: Int,
    val imageurl: String,
    val name: String,
    val timestamp: Long
)
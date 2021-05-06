package ir.co.mazar.model.deceased

data class DeceasedDataModel(
    val birthday: String,
    val deathday: String,
    val id: Int,
    val imageurl: String,
    val name: String,
    val timestamp: Long,
    val recordid: Int
)
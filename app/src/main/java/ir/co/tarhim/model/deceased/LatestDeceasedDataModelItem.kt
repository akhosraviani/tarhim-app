package ir.co.tarhim.model.deceased

data class LatestDeceasedDataModelItem(
    val birthday: Int,
    val deathday: Int,
    val id: Int,
    val imageurl: String,
    val name: String,
    val timestamp: Int
)
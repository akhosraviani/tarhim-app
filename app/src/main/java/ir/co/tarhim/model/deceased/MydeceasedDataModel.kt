package ir.co.tarhim.model.deceased

data class MydeceasedDataModel(
    val accesstype: String,
    val birthday: Int,
    val creator: String,
    val deathday: Int,
    val deathloc: String,
    val description: String,
    val id: Int,
    val imageurl: String,
    val name: String
)
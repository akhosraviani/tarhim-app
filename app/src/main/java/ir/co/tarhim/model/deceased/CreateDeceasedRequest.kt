package ir.co.tarhim.model.deceased

import java.nio.channels.FileLock

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
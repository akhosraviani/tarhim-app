package ir.co.mazar.model

data class RemindeRequestModel(
    val aniversary: Boolean,
    val deceasedId: Int,
    val fifth: Boolean,
    val fortyth: Boolean,
    val mobile: String,
    val seventh: Boolean,
    val third: Boolean,
    val type: String
)
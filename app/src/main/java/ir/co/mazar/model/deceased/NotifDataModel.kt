package ir.co.mazar.model.deceased

data class NotifDataModel(
    val date: Long,
    val deceasedId: Int,
    val id: Int,
    val text: String,
    val type: String,
    val userId: Int
)
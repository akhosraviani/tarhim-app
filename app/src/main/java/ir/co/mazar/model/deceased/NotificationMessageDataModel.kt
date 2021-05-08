package ir.co.mazar.model.deceased

data class NotificationMessageDataModel(
    val date: Int,
    val deceasedId: Int,
    val id: Int,
    val text: String,
    val type: String,
    val userId: Int
)
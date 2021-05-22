package ir.co.mazar.model.deceased

data class MyInboxDataModel(
    val date: Long,
    val message: String,
    val name: String,
    val notificationId: Int,
    val requestId: Int,
    val subject: String,
    val type: String
)
package ir.co.mazar.model.deceased

data class RequirementDataModel(
    val id: Int,
    val imageUrl: String,
    val message: String,
    val name: String,
    val subject: String,
    val timestamp: String,
    val type: String,
    val userId: Int
)
package ir.co.tarhim.model.deceased

data class ReportRequest(
    val reported: Boolean,
    val type: String,
    val typeId: Int
)
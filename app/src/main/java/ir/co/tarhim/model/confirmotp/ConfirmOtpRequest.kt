package ir.co.tarhim.model.confirmotp

data class ConfirmOtpRequest (
    val code : Int ,
    val mobile : String
)
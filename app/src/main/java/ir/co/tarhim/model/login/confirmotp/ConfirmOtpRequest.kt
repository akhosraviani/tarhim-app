package ir.co.tarhim.model.login.confirmotp

data class ConfirmOtpRequest (
    val code : String ,
    val mobile : String
)
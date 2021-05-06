package ir.co.mazar.model.login.confirmotp

data class ConfirmOtpRequest (
    val code : String ,
    val mobile : String
)
package ir.co.mazar.model.login.confirmpass

data class ConfirmPasswordRequest(
    val mobile:String,
    val password:String,
) {
}
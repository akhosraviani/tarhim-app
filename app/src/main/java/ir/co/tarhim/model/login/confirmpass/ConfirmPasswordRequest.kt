package ir.co.tarhim.model.login.confirmpass

data class ConfirmPasswordRequest(
    val mobile:String,
    val password:String,
) {
}
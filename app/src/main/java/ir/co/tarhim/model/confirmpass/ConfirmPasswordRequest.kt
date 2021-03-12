package ir.co.tarhim.model.confirmpass

data class ConfirmPasswordRequest(
    val mobile:String,
    val password:String,
) {
}
package ir.co.tarhim.ui.callback

interface InboxListener {
    fun acceptRequestCallback(notifId:Int)
    fun declineRequestCallback(notifId:Int)
}
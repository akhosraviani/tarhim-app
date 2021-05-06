package ir.co.mazar.ui.callback

interface InboxListener {
    fun acceptRequestCallback(notifId:Int)
    fun declineRequestCallback(notifId:Int)
}
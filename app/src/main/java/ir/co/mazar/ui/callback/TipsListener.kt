package ir.co.mazar.ui.callback

interface TipsListener {
    fun deleteCallback(msgId:Int,reply:Boolean)
    fun reportCallback(msgId:Int,reply:Boolean)
    fun replyCallback(msgId:Int,reply:Boolean)
}
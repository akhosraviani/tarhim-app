package ir.co.tarhim.utils

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import ir.co.tarhim.R
import java.time.Duration

class TarhimToast private constructor(
    builder: TarhimToast.Builder
) {

    private lateinit var toast: Toast
    private val message: String?
    private val activity: Activity?
    private lateinit var view: View

    init {
        this.message = builder.msg
        this.activity=builder.activity
        view = LayoutInflater.from(activity).inflate(R.layout.toast_view, null, false)
        toast = Toast(activity)
        toast.view = view.rootView
        toast.setGravity(Gravity.BOTTOM, 0, 30)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    class Builder() {
        var msg: String? = null
            private set
        var activity: Activity? = null
            private set

        fun message(msgToast: String) = apply {
            this.msg = msgToast
        }
        fun setActivity(activity:Activity)=apply {
            this.activity=activity
        }

        fun build() = TarhimToast(this)

    }

}
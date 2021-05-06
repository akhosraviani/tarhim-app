package ir.co.mazar.utils

import android.content.Context
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ir.co.mazar.R
import java.util.concurrent.TimeUnit


data class TimerConfig(val timeout: Long, val interval: Long)

class Timer(
    private val displayView: AppCompatTextView,
    private val timerConfig: TimerConfig = TimerConfig(
        DEFAULT_TIME_OUT,
        DEFAULT_INTERVAL
    ),
    private val timerStyle: Int = R.string.all_timer_style
) {

    companion object {
        const val DEFAULT_TIME_OUT: Long = 121_000
        const val DEFAULT_INTERVAL: Long = 1_000
    }

    var isStarted: Boolean = false
    private val context: Context = displayView.context
    private var stateChange = false
    private var counter: CountDownTimer? = null

    init {
        start()
    }

    private fun start(stateListener: ((TimerState) -> Unit)? = null) {
        stateChange = true
        counter = object : CountDownTimer(timerConfig.timeout, timerConfig.interval) {
            override fun onFinish() {
                stateChange = true
                isStarted = false
                stateListener?.invoke(TimerState.FINISHED)
                displayView.text = context.resources.getString(R.string.verification_send_me_again)
                displayView.setTextColor(ContextCompat.getColor(context, R.color.grey_900))
            }

            override fun onTick(millisUntilFinished: Long) {
                isStarted = true
                if (stateChange) {
                    stateListener?.invoke(TimerState.STARTED)
                    stateChange = false
                }
                val minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - (minute * 60)
                displayView.text = context.getString(
                    timerStyle,
                    minute, second

                )
            }
        }
        counter?.start()
    }


    fun finish() {
        counter?.onFinish()
    }

    fun sendCodeAgain(): Boolean {
        return if (!isStarted) {
            counter?.onFinish()
            displayView.setTextColor(ContextCompat.getColor(context, R.color.grey_500))
            start()
            true
        } else {
            false
        }
    }

}
package ir.co.mazar.ui.activities.inbox

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.ui.activities.inbox.adapter.InboxRecyclerAdapter
import ir.co.mazar.ui.activities.inbox.adapter.NotificationRecyclerAdapter
import ir.co.mazar.ui.callback.InboxListener
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.inbox_message_activity.*

class NotificationMessageActivity : AppCompatActivity(), InboxListener {

    private lateinit var ViewModel: HomeViewModel
    private lateinit var notifAdapter: NotificationRecyclerAdapter
    private lateinit var viewModel: HomeViewModel
    private var adminStatus: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inbox_message_activity)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        txttitleDeceaedPage.text="صندوق اعلان ها"

        initRecycler()

        viewModel.ldNotificationMessage.observe(this, Observer {
            it.let {
                if (it != null) {
                    if (it.size > 0) {
                        notifAdapter.submitList(it)
                    }
                }
            }
        })



        BtBackInbox.setOnClickListener {
            finish()
        }

    }

    private fun initRecycler() {
        viewModel.requestNotification()
        notifAdapter = NotificationRecyclerAdapter(this)
        inboxMessageRecycler.adapter = notifAdapter
        inboxMessageRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(this, resanim)
        inboxMessageRecycler.layoutAnimation = animation
        inboxMessageRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun acceptRequestCallback(notifId: Int) {
        viewModel.requestAcceptRequest(notifId)
    }

    override fun declineRequestCallback(notifId: Int) {
        viewModel.requestRejectRequest(notifId)
    }
}
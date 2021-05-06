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
import ir.co.mazar.ui.callback.InboxListener
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.inbox_message_activity.*

class InboxMessageActivity : AppCompatActivity(), InboxListener {

    private lateinit var ViewModel: HomeViewModel
    private lateinit var inboxAdapter: InboxRecyclerAdapter
    private lateinit var viewModel: HomeViewModel
    private var adminStatus: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inbox_message_activity)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        initRecycler()

        viewModel.ldInboxMessage.observe(this, Observer {
            it.let {
                if (it != null) {
                    if (it.size > 0) {
                        inboxAdapter.submitList(it)
                    }
                }
            }
        })



        viewModel.ldAcceptRequest.observe(this, Observer {
            when (it.code) {
                200 -> {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message(it.message)
                }
                else -> {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message(it.message)
                }
            }
        })
        viewModel.ldRejectRequest.observe(this, Observer {
            when (it.code) {
                200 -> {
                    viewModel.requestInboxMessage()
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message(it.message)
                }
                else -> {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message(it.message)
                }
            }
        })

        BtBackInbox.setOnClickListener {
            finish()
        }

    }

    private fun initRecycler() {
        viewModel.requestInboxMessage()
        inboxAdapter = InboxRecyclerAdapter(this)
        inboxMessageRecycler.adapter = inboxAdapter
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
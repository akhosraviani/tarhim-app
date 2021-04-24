package ir.co.tarhim.ui.activities.inbox

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.ui.activities.inbox.adapter.InboxRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.inbox_message_activity.*

class InboxMessageActivity : AppCompatActivity() {

    private lateinit var ViewModel: HomeViewModel
    private lateinit var inboxAdapter: InboxRecyclerAdapter
    private lateinit var viewModel: HomeViewModel

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



        BtBackInbox.setOnClickListener {
            finish()
        }

    }

    private fun initRecycler() {
        viewModel.requestInboxMessage()
        inboxAdapter = InboxRecyclerAdapter()
        inboxMessageRecycler.adapter = inboxAdapter
        inboxMessageRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(this, resanim)
        inboxMessageRecycler.layoutAnimation = animation
    }
}
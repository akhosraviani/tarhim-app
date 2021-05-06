package ir.co.mazar.ui.activities.invite_friend

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.ui.activities.invite_friend.adapter.InviteAdapterRecycler
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.contact_fragment.*

class InviteFriendsActivity() : AppCompatActivity() {

    companion object{
        private const val TAG = "InviteFriendsActivity"
    }
    
    private lateinit var bottomSheet: FrameLayout
    private lateinit var manager: LinearLayoutManager
    private var popupState = false
    private var deceasedId: Int = -1
    private lateinit var viewModel: HomeViewModel
    private lateinit var imm: InputMethodManager
    private lateinit var inviteAdapter: InviteAdapterRecycler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_fragment)

        if (intent?.getIntExtra("DeceasedId", -1) != null) {
            deceasedId = intent?.getIntExtra("DeceasedId", -1)!!
        }
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        viewModel.requestFollowes(deceasedId)

        Log.e(TAG, "onCreate: "+deceasedId )
        viewModel.ldFollowersList.observe(this, Observer {
            it.also {

                Log.e(TAG, "onCreate: "+it.size )
                inviteAdapter.submitList(it)
            }
        })

        viewModel.ldInvite.observe(this, Observer {
            it.also {
                when (it.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                        viewModel.requestFollowes(deceasedId)
                    }
                    400->{
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                    }
                }
            }
        })


        BtBackInvite.setOnClickListener {
            finish()
        }
        initContactRecycler()

        showAddFriendLayout()
    }


    private fun initContactRecycler() {
        inviteAdapter = InviteAdapterRecycler()
        contactRecycler.adapter = inviteAdapter
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactRecycler.layoutManager = manager
        contactRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(this, R.anim.up_to_bottom)
    }


    private fun showAddFriendLayout() {

        AddFriendsTv.setOnClickListener {
            if (!TextUtils.isEmpty(ETInputNumber.text) && ETInputNumber.text!!.length == 11) {
                viewModel.requestInvite(deceasedId, ETInputNumber.text.toString())
                ETInputNumber.setText("")
                closeKeyboard(window!!.decorView)
            } else {
                TarhimToast.Builder()
                    .setActivity(this)
                    .message("شماره به درستی وارد نشده است")
                    .build()

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun closeKeyboard(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    var contentView = view.findViewById<View>(android.R.id.content)
                    var rect = Rect()
                    view.getWindowVisibleDisplayFrame(rect)
                    var screenHeight = contentView.height
                    val keypadHeight: Int = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        imm.hideSoftInputFromWindow(contentView!!.getWindowToken(), 0)

                    } else {
                        return
                    }

                }

            }
        }
    }

}
package ir.co.mazar.ui.activities.invite_friend

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.ui.activities.invite_friend.adapter.InviteAdapterRecycler
import ir.co.mazar.ui.callback.InviteFriendsListener
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.contact_fragment.*
import kotlinx.android.synthetic.main.item_contact.*


class InviteActivity : AppCompatActivity(), InviteFriendsListener {
    companion object {
        private const val TAG = "InviteFriendsActivity"
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    private lateinit var bottomSheet: FrameLayout
    private lateinit var manager: LinearLayoutManager
    private var popupState = false
    private var isShare = false
    private var deceasedId: Int = -1
    private lateinit var viewModel: HomeViewModel
    private lateinit var imm: InputMethodManager
    private lateinit var inviteAdapter: InviteAdapterRecycler
    private var contactList = ArrayList<ContactModel>()
    private var addedContacts: MutableList<ContactModel> = mutableListOf()

    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_fragment)


        if (intent?.getIntExtra("DeceasedId", -1) != null) {
            intent?.getIntExtra("DeceasedId", -1)?.let {
                deceasedId = it
            }
        }
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        builder = AlertDialog.Builder(this)
        builder.setTitle("Read contacts access needed")
        builder.setMessage("Please enable access to contacts.")
        showcontact.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )



            if (permission == PackageManager.PERMISSION_GRANTED
            ) {
                showLoading(true)


                viewModel.getContactList(this)
//                fetchContactsCProviderClient()


            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }

//        getContactList()

        viewModel.muList.observe(this, Observer {
            if (it.size > 0) {
                var ls = it.sortedBy { it.name }.distinctBy { it.name }
                inviteAdapter = InviteAdapterRecycler(this, ls, this)
                manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                contactRecycler.adapter = inviteAdapter
                contactRecycler.layoutManager = manager
                contactRecycler.layoutAnimation =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.up_to_bottom)
                showLoading(false)
            }
        })


        viewModel.ldInvite.observe(this, Observer {
            it.also {
                when (it.code) {

                    200 -> {
                        showLoading(false)
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
                        viewModel.requestAddContact(deceasedId)

                    }
                    400 -> {
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
                    }
                }
            }
        })

        viewModel.ldShareLink.observe(this, Observer {
            if (it != null) {
                showLoading(false)
                if (isShare) {
                    val clipboard: ClipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Link", it.message)
                    clipboard.setPrimaryClip(clip)

                    TarhimToast.Builder()
                        .setActivity(this)
                        .message("لینک دعوت کپی شد")
                        .build()
                } else {
                    shareLink(it.message)
                }
            }
        })

        BtBackInvite.setOnClickListener {
            finish()
        }

        showAddFriendLayout()


        AddLinkTv.setOnClickListener {
            ETInputNumber.text?.let {
                if (!TextUtils.isEmpty(ETInputNumber.text) &&
                    it.length == 11 && it.startsWith("09")
                ) {
                    isShare = true
                    viewModel.requestShareLink(deceasedId, ETInputNumber.text.toString())
                    showLoading(true)
                } else {
                    TarhimToast.Builder()
                        .setActivity(this)
                        .message("موبایل به درستی وارد نشده! ")
                        .build()
                }
            }
        }

        viewModel.livedataListContact.observe(this, Observer {
            if (it.size > 0) {
                inviteAdapter = InviteAdapterRecycler(this, it, this)
                manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                contactRecycler.adapter = inviteAdapter
                contactRecycler.layoutManager = manager
                contactRecycler.layoutAnimation =
                    AnimationUtils.loadLayoutAnimation(this, R.anim.up_to_bottom)
                showLoading(false)
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getContactList(this)
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }


    private fun showAddFriendLayout() {

        AddFriendsTv.setOnClickListener {
            if (!TextUtils.isEmpty(ETInputNumber.text) && ETInputNumber.text!!.length == 11) {
                addContact(ETInputNumber.text.toString())
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

    private fun closeKeyboard(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val contentView = view.findViewById<View>(android.R.id.content)
                    val rect = Rect()
                    view.getWindowVisibleDisplayFrame(rect)
                    val screenHeight = contentView.height
                    val keypadHeight: Int = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        imm.hideSoftInputFromWindow(contentView!!.windowToken, 0)

                    } else {
                        return
                    }

                }

            }
        }

    }

    override fun addContact(phone: String) {

        viewModel.requestInvite(deceasedId, phone)
        showLoading(true)

    }

    override fun sendInvitationLInk(phone: String) {
        if (phone.isNotEmpty()) {
            viewModel.requestShareLink(deceasedId, phone)
            showLoading(true)
//            Toast.makeText(this, "$phone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(status: Boolean) {
        when (status) {
            true -> {
                loadingContactFragment.visibility = View.VISIBLE
                this?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

            }
            else -> {
                loadingContactFragment.visibility = View.GONE
                this?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    private fun shareLink(msg: String) {
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg)
        startActivity(Intent.createChooser(shareIntent, ""))
    }


    private fun fetchContactsCProviderClient(): ArrayList<ContactModel?>? {
        var mContactList: ArrayList<ContactModel?>? = null
        val cr = contentResolver
        val cursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {
                val nameIndex: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var name: String
                var number: String
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex)
                    number = cursor.getString(numberIndex)
                    Log.e(TAG, "fetchContactsCProviderClient: $name")
                    Log.e(TAG, "fetchContactsCProviderClient: $number")
                }
            } finally {
                cursor.close()
            }
        }
        return mContactList
    }


    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )


}
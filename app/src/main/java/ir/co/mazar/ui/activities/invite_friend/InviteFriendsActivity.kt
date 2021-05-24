package ir.co.mazar.ui.activities.invite_friend

import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.provider.Contacts
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.ui.activities.invite_friend.adapter.InviteAdapterRecycler
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.contact_fragment.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.jar.Manifest

class InviteFriendsActivity() : AppCompatActivity() {

    companion object{
        private const val TAG = "InviteFriendsActivity"
        val REQUEST_PERMISSION = 1
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

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS) , REQUEST_PERMISSION)
        }else{
            Log.i("testTag00","contact")
            getContacts()
        }
//        viewModel.requestFollowes(deceasedId)

//        Log.e(TAG, "onCreate: "+deceasedId )
//        viewModel.ldFollowersList.observe(this, Observer {
//            it.also {
//             Log.i("testTag0","hi injam")
//                Log.e(TAG, "onCreate: "+it.size )
//                inviteAdapter.submitList(it)
//            }
//        })

        viewModel.ldInvite.observe(this, Observer {
            it.also {
                when (it.code) {

                    200 -> {

                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
//                        viewModel.requestFollowes(deceasedId)
                    }
                    400->{
                        TarhimToast.Builder()
                            .setActivity(this)
                            .message(it.message)
                            .build()
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
//        inviteAdapter = InviteAdapterRecycler()
//        contactRecycler.adapter = inviteAdapter
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_PERMISSION) getContacts()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getContacts() {
        val adapter = InviteAdapterRecycler( this , getContactsData())
        contactRecycler.adapter = adapter

        Log.i("testTag00","my contacts= "+getContactsData().toString())
    }

    private fun getContactsData(): ArrayList<ContactModel> {
     val contactList = ArrayList<ContactModel>()
        val contactCurser = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)

        if((contactCurser?.count ?: 0 ) > 0 ){
            while (contactCurser != null && contactCurser.moveToNext()){
                val rowId = contactCurser.getString(contactCurser.getColumnIndex(ContactsContract.Contacts._ID))
                val name = contactCurser.getString(contactCurser.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber = ""
                if(contactCurser.getInt(contactCurser.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0 ) {
                    val phoneNumberCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                        arrayOf<String>(rowId),
                        null
                    )
                    while (phoneNumberCursor!!.moveToNext()) {
                        phoneNumber += phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        ) + "\n"
                    }
                    phoneNumberCursor.close()
                }

                    contactList.add(ContactModel(phoneNumber,name))
                }
            }
        contactCurser!!.close()
        return contactList
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

}
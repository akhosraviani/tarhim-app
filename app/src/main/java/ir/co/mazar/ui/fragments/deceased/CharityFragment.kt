package ir.co.mazar.ui.fragments.deceased

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.RemindeRequestModel
import ir.co.mazar.model.deceased.PrayDeceasedDataModel
import ir.co.mazar.model.deceased.PrayDeceasedRequest
import ir.co.mazar.ui.callback.CharityListener
import ir.co.mazar.ui.callback.SpiritualListener
import ir.co.mazar.ui.fragments.deceased.adapter.CharityRecyclerAdapter
import ir.co.mazar.ui.fragments.deceased.adapter.SpiritualRecyclerAdapter
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.PrayDeceasedType
import ir.co.mazar.utils.TarhimConfig
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_charity.*

class CharityFragment : Fragment(), CharityListener, SpiritualListener {

    companion object {
        private const val TAG = "CharityFragment"
        private const val DECEASED_ID = "deceasedId"
    }

    val prayList =
        arrayOf("ذکر صلوات", "ختم قرآن", "روزه", "نماز", "سوره های برگزیده", "نماز وحشت")
    private lateinit var dialog: Dialog


    private var deceasedId: Int? = null

    fun newInstance(deceasedId: Int): CharityFragment {
        val fragment = CharityFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putInt(DECEASED_ID, deceasedId)
        return fragment
    }


    private lateinit var charityAdapter: CharityRecyclerAdapter
    private lateinit var spiritualAdapter: SpiritualRecyclerAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        deceasedId = requireArguments().getInt(DECEASED_ID)
        initCharityRecycler()


        viewModel.requestGetCharity()
        viewModel.getSiritualRes(deceasedId!!)



        viewModel.ldCharity.observe(viewLifecycleOwner, Observer {

            it.also {
                charityAdapter.submitList(it)
            }
        })


        viewModel.ldSiritual.observe(viewLifecycleOwner, Observer {
            it.also {
                when (it.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(it.message+" و به خانواده ی ایشان اطلاع داده خواهد شد")
                            .build()
                        viewModel.getSiritualRes(deceasedId!!)
                    }
                }
            }
        })


        viewModel.ldGetSiritual.observe(viewLifecycleOwner, Observer {
            it.also {

                if (it != null && it.isNotEmpty()) {
                    initSpiritualRecycler(it)
                } else {
                    initSpiritualRecycler(ArrayList())
                }
            }
        })


    }


    private fun initCharityRecycler() {
        charityAdapter = CharityRecyclerAdapter(this)
        charitycenterRecycler.adapter = charityAdapter
        charitycenterRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        charitycenterRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
    }

    private fun initSpiritualRecycler(list: List<PrayDeceasedDataModel>) {
        spiritualAdapter = SpiritualRecyclerAdapter(prayList, list, this)
        spiritualRecycler.adapter = spiritualAdapter
        spiritualRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        spiritualRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
        spiritualAdapter.notifyDataSetChanged()
    }

    override fun chalityCallback(charityUrl: String) {
        Log.e(TAG, "chalityCallback: " + charityUrl)
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(charityUrl)
        startActivity(i)

    }

    override fun spiritualCallback(Id: Int) {
        prayDialog(Id)
    }

    private fun prayDialog(id: Int) {

        Log.i("testTag4", "dialog")
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_charity)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val charityTitle: TextView = dialog.findViewById(R.id.charityTitle)
        val charityYes: TextView = dialog.findViewById(R.id.charityYes)
        val charityNo: TextView = dialog.findViewById(R.id.charityNo)

        when (id) {
            0 -> charityTitle.text =
                resources.getString(R.string.charity_type_title, (" صلوات ختم کنید؟").toString())
            1 -> charityTitle.text =
                resources.getString(R.string.charity_type_title, (" قرآن ختم کنید؟").toString())
            2 -> charityTitle.text =
                resources.getString(R.string.charity_type_title, (" روزه بگیرید؟").toString())
            3 -> charityTitle.text =
                resources.getString(R.string.charity_type_title, (" نماز بخوانید؟").toString())
            4 -> charityTitle.text = resources.getString(
                R.string.charity_type_title,
                (" سوره های برگزیده را تلاوت نمایید؟").toString()
            )
            5 -> charityTitle.text =
                resources.getString(R.string.charity_type_title, (" نماز وحشت بخوانید؟").toString())
        }

        charityYes.setOnClickListener {
            when (id) {
                0 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.Salavat.name
                    )
                )
                1 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.Quran.name
                    )
                )
                2 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.Rooze.name
                    )
                )
                3 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.Namaz.name
                    )
                )
                4 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.Sore.name
                    )
                )
                5 -> viewModel.requestSiritual(
                    PrayDeceasedRequest(
                        deceasedId!!,
                        PrayDeceasedType.NamazVahshat.name
                    )
                )
            }
        }
        charityNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
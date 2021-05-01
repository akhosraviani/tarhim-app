package ir.co.tarhim.ui.fragments.deceased

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.PrayDeceasedDataModel
import ir.co.tarhim.model.deceased.PrayDeceasedRequest
import ir.co.tarhim.ui.callback.CharityListener
import ir.co.tarhim.ui.callback.SpiritualListener
import ir.co.tarhim.ui.fragments.deceased.adapter.CharityRecyclerAdapter
import ir.co.tarhim.ui.fragments.deceased.adapter.SpiritualRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.PrayDeceasedType
import ir.co.tarhim.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_charity.*

class CharityFragment : Fragment(), CharityListener, SpiritualListener {

    companion object {
        private const val TAG = "CharityFragment"
        private const val DECEASED_ID = "deceasedId"
    }

    val prayList =
        arrayOf("ذکر صلوات", "ختم قرآن", "روزه", "نماز", "سوره های برگزیده", "نماز وحشت")


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
                            .message(it.message)
                            .build()
                        viewModel.getSiritualRes(deceasedId!!)
                    }
                }
            }
        })


        viewModel.ldGetSiritual.observe(viewLifecycleOwner, Observer {
            it.also {
                Log.e(TAG, "onViewCreated: " + it.size)

                if (it != null && it.size > 0) {
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
//        Toast.makeText(requireActivity(), "test charity click button", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "chalityCallback: "+charityUrl )
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(charityUrl)
        startActivity(i)

    }

    override fun spiritualCallback(Id: Int) {
//        Toast.makeText(requireContext(), "$Id", Toast.LENGTH_SHORT).show()
        when (Id) {
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


}
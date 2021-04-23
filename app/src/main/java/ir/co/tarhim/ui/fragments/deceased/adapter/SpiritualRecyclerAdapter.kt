package ir.co.tarhim.ui.fragments.deceased.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.PrayDeceasedDataModel
import ir.co.tarhim.ui.callback.SpiritualListener
import ir.co.tarhim.utils.PrayDeceasedType
import kotlinx.android.synthetic.main.row_charity_recycler.view.*

class SpiritualRecyclerAdapter(
    var prayList: Array<String>,
//    var spiritualList: List<PrayDeceasedDataModel>,
    var spiritualListener: SpiritualListener
) :
    RecyclerView.Adapter<SpiritualRecyclerAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var TvBadgeNotif: AppCompatTextView
        lateinit var BtnCharity: AppCompatButton


        init {
            BtnCharity = view.BtnCharity
            TvBadgeNotif = view.badge_notification

        }


        open fun bind(prayName:String,prayCount:Int) {

            BtnCharity.setText(prayName)
//            TvBadgeNotif.setText(prayCount)
            if (prayCount >= 100)
                TvBadgeNotif.textSize = 13F

            if (prayCount >= 1000)
                TvBadgeNotif.textSize = 9F
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpiritualRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_charity_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(position, spiritualList!![position])
        var prayCount:Int?=0
//        when(position){
//            0->prayCount=spiritualList[position].salavat
//            1->prayCount=spiritualList[position].quran
//            2->prayCount=spiritualList[position].rooze
//            3->prayCount=spiritualList[position].namaz
//            4->prayCount=spiritualList[position].sore
//            5->prayCount=spiritualList[position].namazvahshat
//
//        }
        holder.bind(prayList[position],prayCount!!)



        holder.BtnCharity.setOnClickListener {
            spiritualListener.spiritualCallback(holder.adapterPosition)
        }


    }

    override fun getItemCount(): Int {
        return prayList!!.size
    }


}
package ir.co.mazar.ui.callback

import ir.co.mazar.model.deceased.MyDeceasedDataModel

interface ProfileListener {
    interface MyDeceasedEditCallBack{
        fun editDeceased(item:MyDeceasedDataModel)
    }
    interface MyDeceasedListener{
        fun myDeceasedCallBack(deceasedId:Int)
    }
    interface UnFollowDeceasedListener{
        fun unFollowCallBack(deceasedId:Int)
    }
}
package ir.co.tarhim.ui.callback

import ir.co.tarhim.model.deceased.MyDeceasedDataModel

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
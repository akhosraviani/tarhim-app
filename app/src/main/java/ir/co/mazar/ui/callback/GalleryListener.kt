package ir.co.mazar.ui.callback

import ir.co.mazar.model.deceased.GalleryDataModel

interface GalleryListener {
    fun galleryRecyclerCallBack(position:Int,item:GalleryDataModel)
}
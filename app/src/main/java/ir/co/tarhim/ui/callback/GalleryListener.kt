package ir.co.tarhim.ui.callback

import ir.co.tarhim.model.deceased.GalleryDataModel

interface GalleryListener {
    fun galleryRecyclerCallBack(item:GalleryDataModel)
}
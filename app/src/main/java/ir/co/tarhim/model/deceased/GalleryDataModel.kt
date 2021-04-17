package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class GalleryDataModel(
    val id: Int,
    val imagespath: Any
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        TODO("imagespath")
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<GalleryDataModel> {
        override fun createFromParcel(parcel: Parcel): GalleryDataModel {
            return GalleryDataModel(parcel)
        }

        override fun newArray(size: Int): Array<GalleryDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
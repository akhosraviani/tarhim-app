package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable

data class GalleryDataModel(
    val id: Int,
    val imagespath: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeStringList(imagespath)
    }

    override fun describeContents(): Int {
        return 0
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
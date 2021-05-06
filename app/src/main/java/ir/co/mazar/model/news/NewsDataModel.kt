package ir.co.mazar.model.news

import android.os.Parcel
import android.os.Parcelable

data class NewsDataModel(
    val create_time: Long,
    val id: Int,
    val imageurl: String?,
    val text: String?,
    val topic: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(create_time)
        parcel.writeInt(id)
        parcel.writeString(imageurl)
        parcel.writeString(text)
        parcel.writeString(topic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsDataModel> {
        override fun createFromParcel(parcel: Parcel): NewsDataModel {
            return NewsDataModel(parcel)
        }

        override fun newArray(size: Int): Array<NewsDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
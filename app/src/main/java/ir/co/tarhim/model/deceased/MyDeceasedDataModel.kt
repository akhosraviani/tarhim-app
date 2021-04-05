package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable

data class MyDeceasedDataModel(
    val accesstype: String?,
    val birthday: String,
    val creator: String,
    val deathday: String,
    val deathloc: String,
    val description: String,
    val id: Int,
    val imageurl: String,
    val name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accesstype)
        parcel.writeString(birthday)
        parcel.writeString(creator)
        parcel.writeString(deathday)
        parcel.writeString(deathloc)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeString(imageurl)

        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyDeceasedDataModel> {
        override fun createFromParcel(parcel: Parcel): MyDeceasedDataModel {
            return MyDeceasedDataModel(parcel)
        }

        override fun newArray(size: Int): Array<MyDeceasedDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
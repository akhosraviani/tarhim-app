package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable
import java.nio.channels.FileLock

data class DeceasedProfileDataModel(
    val accesstype: String,
    val birthday: String,
    val deathday: String,
    val deathloc: String,
    val description: String,
    val followerCount: Int,
    val imageurl: String,
    val isfollow: Boolean,
    val isowner: Boolean,
    val isrequested: Boolean,
    val latitude: Double,
    val longitude: Double,
    val name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()!!,
        parcel.readByte() != 0.toByte()!!,
        parcel.readByte() != 0.toByte()!!,
        parcel.readDouble()!!,
        parcel.readDouble()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accesstype)
        parcel.writeString(birthday)
        parcel.writeString(deathday)
        parcel.writeString(deathloc)
        parcel.writeString(description)
        parcel.writeInt(followerCount)
        parcel.writeString(imageurl)
        parcel.writeByte(if (isfollow) 1 else 0)
        parcel.writeByte(if (isowner) 1 else 0)
        parcel.writeByte(if (isrequested) 1 else 0)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeceasedProfileDataModel> {
        override fun createFromParcel(parcel: Parcel): DeceasedProfileDataModel {
            return DeceasedProfileDataModel(parcel)
        }

        override fun newArray(size: Int): Array<DeceasedProfileDataModel?> {
            return arrayOfNulls(size)
        }
    }
}




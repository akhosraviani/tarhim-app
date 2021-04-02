package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class DeceasedProfileDataModel(
    val name: String?,
    val description: String?,
    val birthday: String?,
    val deathday: String?,
    val deathloc: String?,
    val imageurl: String?,
    val latitude: Long?,
    val longitude: Long?,
    val isowner: Boolean?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
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
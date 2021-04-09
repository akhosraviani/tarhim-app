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
    val isowner: Boolean?,
    val isfollow: Boolean?,
    val isrequested: Boolean?,
    val accesstype: String?,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
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


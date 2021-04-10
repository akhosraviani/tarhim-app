package ir.co.tarhim.model.deceased

import android.os.Parcel
import android.os.Parcelable

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(birthday)
        parcel.writeString(deathday)
        parcel.writeString(deathloc)
        parcel.writeString(imageurl)
        parcel.writeValue(isowner)
        parcel.writeValue(isfollow)
        parcel.writeValue(isrequested)
        parcel.writeString(accesstype)
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



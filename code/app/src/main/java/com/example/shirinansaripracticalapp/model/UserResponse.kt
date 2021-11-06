package com.example.shirinansaripracticalapp.model

import android.os.Parcel
import android.os.Parcelable

data class UserResponse(
    var results: ArrayList<UserItem>
) {

    data class UserItem(
        var gender: String?,
        var name: UserName? = null,
        var location: UserLocation? = null,
        var email: String?,
        var phone: String?,
        var login: UserLogin? = null,
        var picture: UserPicture? = null

    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            gender = parcel.readString(),
            name = parcel.readArrayList(UserName::class.java.classLoader) as UserName,
            location = parcel.readArrayList(UserLocation::class.java.classLoader) as UserLocation,
            email = parcel.readString(),
            phone = parcel.readString(),
            picture = parcel.readArrayList(UserPicture::class.java.classLoader) as UserPicture
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(gender)
            parcel.writeString(email)
            parcel.writeString(phone)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UserItem> {
            override fun createFromParcel(parcel: Parcel): UserItem {
                return UserItem(parcel)
            }

            override fun newArray(size: Int): Array<UserItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class UserName(
        var title: String?,
        var first: String?,
        var last: String?
    )

    data class UserLocation(
        var city: String?,
        var state: String?,
        var country: String?,
        var postcode: String?,
        var coordinates: UserCoordinates? = null
    )

    data class UserCoordinates(
        var latitude: String?,
        var longitude: String?
    )

    data class UserPicture(
        var large: String?,
        var medium: String?,
        var thumbnail: String?
    )

    data class UserLogin(
        var uuid: String?
    )
}
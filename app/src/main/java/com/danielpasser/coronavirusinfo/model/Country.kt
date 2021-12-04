package com.danielpasser.coronavirusinfo.model

import android.os.Parcel
import android.os.Parcelable


data class Country(
    val ID: String?,
    val Country: String?,
    val CountryCode: String?,
    val Slug: String?,
    val NewConfirmed: Int,
    val TotalConfirmed: Int,
    val NewDeaths: Int,
    val TotalDeaths: Int,
    val NewRecovered: Int,
    val TotalRecovered: Int,
    val Date: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ID)
        parcel.writeString(Country)
        parcel.writeString(CountryCode)
        parcel.writeString(Slug)
        parcel.writeInt(NewConfirmed)
        parcel.writeInt(TotalConfirmed)
        parcel.writeInt(NewDeaths)
        parcel.writeInt(TotalDeaths)
        parcel.writeInt(NewRecovered)
        parcel.writeInt(TotalRecovered)
        parcel.writeString(Date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}
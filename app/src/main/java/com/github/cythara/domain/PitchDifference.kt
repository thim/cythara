package com.github.cythara.domain

import android.os.Parcel
import android.os.Parcelable

class PitchDifference : Parcelable {
    val closest: Note
    val deviation: Double

    constructor(closest: Note, deviation: Double) {
        this.closest = closest
        this.deviation = deviation
    }

    private constructor(parcel: Parcel) {
        closest = Note.createNote(parcel.readString())
        deviation = parcel.readDouble()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(closest.getShortRep())
        dest.writeDouble(deviation)
    }

    companion object CREATOR : Parcelable.Creator<PitchDifference> {
        override fun createFromParcel(parcel: Parcel): PitchDifference {
            return PitchDifference(parcel)
        }

        override fun newArray(size: Int): Array<PitchDifference?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.mapsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationEntity(
  val latitude: Double,
  val longitude: Double
): Parcelable

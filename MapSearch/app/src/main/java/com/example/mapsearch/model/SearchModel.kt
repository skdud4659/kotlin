package com.example.mapsearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// 데이터 꾸러미가 A Activity에서 B Activity로 한꺼번에 전달되고 받아 볼 수 있도록 하기 위해.
@Parcelize
data class SearchResultEntity(
  val fullAddress: String,
  val name: String,
  val locationLatLng: LocationEntity
): Parcelable

package com.example.mapsearch.model

data class SearchModel(
  val fullAddress: String,
  val name: String,
  val locationLatLng: LocationLatLngModel
)

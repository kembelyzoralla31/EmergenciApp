package com.mkiperszmid.travelguideai.home.domain.model

data class HomeFilterSettings(
    val people: Int = 1,
    val hospitales: Boolean = false,
    val veterinarias: Boolean = false
)

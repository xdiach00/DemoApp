package com.xdiach.demoapp.data

data class ObjectModel(
    val uuid: String?,
    val name: String?,
    val description: String?,
    val image: String?,
    val thumbnail_image: String?,
    val location: Location,
    val url: String?,
    val address: Address
)

data class Address (
    val city: String?,
    val street: String?,
    val zip: String?,
    val country: String?
        )
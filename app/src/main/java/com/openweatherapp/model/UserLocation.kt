package com.openweatherapp.model

class UserLocation {

    var isValidLocation = false
    var countryCode: String? = null
    var userAddress: String? = null
    var userLocality: String? = null
    var userSubAdminArea: String? = null
    var userAdminArea: String? = null
    var userPostalCode: String? = null

    constructor() {}

    constructor(
        userAddress: String?,
        userLocality: String?,
        userSubAdminArea: String?,
        userAdminArea: String?,
        userPostalCode: String?
    ) {
        this.userAddress = userAddress
        this.userLocality = userLocality
        this.userSubAdminArea = userSubAdminArea
        this.userAdminArea = userAdminArea
        this.userPostalCode = userPostalCode
    }
}
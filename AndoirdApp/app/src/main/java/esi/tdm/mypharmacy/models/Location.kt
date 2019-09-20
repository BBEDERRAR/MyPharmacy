package esi.tdm.mypharmacy.models

public class MyLocation constructor(lat: Double, lng: Double) {
    var lat: Double
    var lng: Double

    init {
        this.lat = lat
        this.lng = lng
    }
}
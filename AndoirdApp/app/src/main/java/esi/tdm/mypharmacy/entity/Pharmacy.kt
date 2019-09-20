package esi.tdm.mypharmacy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "pharmacies")
data class Pharmacy(
    @PrimaryKey
    var id: Int,
    var city_id: Int,
    var longitude: Int,
    var latitude: Int,
    var name: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var check_in: String? = "",
    var check_out: String? = "",
    var facebook_page: String? = ""
) : Serializable
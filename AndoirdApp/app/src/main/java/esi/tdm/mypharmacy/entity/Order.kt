package esi.tdm.mypharmacy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    var id: Int,
    var pharmacy_id: Int,
    var user_id: Int,
    var img: String,
    var status: String
) : Serializable

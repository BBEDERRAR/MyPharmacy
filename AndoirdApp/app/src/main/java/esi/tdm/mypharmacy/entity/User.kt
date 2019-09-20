package esi.tdm.mypharmacy.entity

import androidx.room.Entity
import java.io.Serializable


@Entity(tableName = "users")
data class User(
    var id:Int,
    var firstName:String,
    var lastName:String,
    var phone:String,
    var nss:String,
    var activated:Boolean,
    var token:String
): Serializable
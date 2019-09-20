package esi.tdm.mypharmacy.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cities")
data class City(
    @PrimaryKey
    @ColumnInfo(name="city_id")
    var id:Int,
    var name:String
): Serializable
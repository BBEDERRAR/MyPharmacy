package esi.tdm.mypharmacy.roomDatabase

import android.content.Context
import androidx.room.Room

object RoomService {

    lateinit var context: Context

    val appDataBase by lazy {
        Room.databaseBuilder(context,AppDataBase::class.java,"dbPharmacyApp").allowMainThreadQueries().build()
    }
}
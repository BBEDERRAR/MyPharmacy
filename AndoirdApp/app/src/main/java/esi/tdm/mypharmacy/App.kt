package esi.tdm.mypharmacy

import android.app.Application
import esi.tdm.mypharmacy.roomDatabase.RoomService


class App: Application(){
    override fun onCreate() {
        super.onCreate()
        RoomService.context = applicationContext
    }
}
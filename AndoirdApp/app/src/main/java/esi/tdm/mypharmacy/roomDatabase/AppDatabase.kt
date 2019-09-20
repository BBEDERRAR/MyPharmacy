package esi.tdm.mypharmacy.roomDatabase

import androidx.room.*
import esi.tdm.mypharmacy.entity.City
import esi.tdm.mypharmacy.entity.Order
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.roomDao.CityDao
import esi.tdm.mypharmacy.roomDao.OrderDao
import esi.tdm.mypharmacy.roomDao.PharmacyDao


@Database(
    entities = arrayOf(
        City::class,
        Pharmacy::class,
        Order::class
    ), version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getCityDao(): CityDao
    abstract fun getPharmacyDao(): PharmacyDao
    abstract fun getOrderDao(): OrderDao

}
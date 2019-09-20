package esi.tdm.mypharmacy.roomDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import esi.tdm.mypharmacy.entity.City

@Dao
interface CityDao {

    @Query("select * from cities")
    fun getCities():List<City>

    @Query("select * from cities where city_id=:idCity")
    fun getCityById(idCity:Int):City

     @Insert
    fun addCities(cities:List<City>?)

    @Update
    fun updateCity(city: City)


}
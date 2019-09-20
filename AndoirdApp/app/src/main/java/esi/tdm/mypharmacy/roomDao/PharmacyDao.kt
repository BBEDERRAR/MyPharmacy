package esi.tdm.mypharmacy.roomDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import esi.tdm.mypharmacy.entity.Pharmacy

@Dao
interface PharmacyDao {

    @Query("select * from pharmacies")
    fun getPharmacies(): List<Pharmacy>

    @Query("select * from pharmacies where id=:idPharmacy")
    fun getPharmacyById(idPharmacy: Int): Pharmacy

    @Query("select * from pharmacies where city_id=:cityId")
    fun getPharmacyByCityId(cityId: Int): List<Pharmacy>

    @Insert
    fun addPharmacies(pharmacies: List<Pharmacy>?)

    @Insert
    fun addPharmacy(pharmacy: Pharmacy?)

    @Update
    fun updatePharmacy(city: Pharmacy)

}
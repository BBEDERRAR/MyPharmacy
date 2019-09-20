package esi.tdm.mypharmacy.roomDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import esi.tdm.mypharmacy.entity.Order

@Dao
interface OrderDao {

    @Query("select * from orders")
    fun getOrders(): List<Order>

    @Insert
    fun addOrders(orders: List<Order>?)

    @Query("select * from orders where id=:idOrder")
    fun getOrderById(idOrder: Int): List<Order>

    @Insert
    fun addOrder(order: Order?)

    @Update
    fun updateOrder(Order: Order)


}
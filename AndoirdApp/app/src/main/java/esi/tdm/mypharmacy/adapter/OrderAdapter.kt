package esi.tdm.mypharmacy.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.config.imageBaseUrl
import esi.tdm.mypharmacy.entity.Order
import esi.tdm.mypharmacy.roomDatabase.RoomService

class OrderAdapter(_ctx: Context, _listOrders: List<Order>) : BaseAdapter() {
    var ctx = _ctx
    val listOrders = _listOrders

    private data class ViewHolder(
        var idOrder: TextView,
        var pharmacyName: TextView,
        var pharmacyAddress: TextView,
        var status: TextView
    )


    override fun getItem(p0: Int) = listOrders.get(p0)

    override fun getItemId(p0: Int) = listOrders.get(p0).hashCode().toLong()

    override fun getCount() = listOrders.size

    override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {

        var view = p0
        var viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.order_layout, parent, false)
            val orderId = view?.findViewById<TextView>(R.id.orderId) as TextView
            val pharmacyName = view?.findViewById<TextView>(R.id.pharmacyName) as TextView
            val pharmacyAddress = view?.findViewById<TextView>(R.id.pharmacyAddress) as TextView
            val status = view?.findViewById<TextView>(R.id.status) as TextView
            viewHolder = ViewHolder(orderId, pharmacyName, pharmacyAddress, status)
            view.setTag(viewHolder)
        } else {
            viewHolder = view.getTag() as ViewHolder
        }

        viewHolder.idOrder.setText("Order Number: #"+listOrders.get(position).id.toString())
        if (listOrders.get(position).status=="pending"){
            viewHolder.status.setTextColor(Color.parseColor("#4A7B9D"))
        }else if (listOrders.get(position).status=="in processing"){
            viewHolder.status.setTextColor(Color.parseColor("#F4A259"))
        }else if (listOrders.get(position).status=="finished"){
            viewHolder.status.setTextColor(Color.parseColor("#8CB369"))
        }
        viewHolder.status.setText(listOrders.get(position).status)


        var pharmacy =
            RoomService.appDataBase.getPharmacyDao().getPharmacyById(listOrders.get(position).pharmacy_id.toInt())

        if (pharmacy != null) {
            viewHolder.pharmacyAddress.setText(pharmacy.address)
            viewHolder.pharmacyName.setText(pharmacy.name)
        }

        return view

    }

}
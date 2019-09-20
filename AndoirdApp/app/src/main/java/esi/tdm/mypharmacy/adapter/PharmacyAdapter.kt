package esi.tdm.mypharmacy.adapter

import android.content.Context
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
import esi.tdm.mypharmacy.entity.Pharmacy

class PharmacyAdapter(_ctx: Context, _listPharmacies: List<Pharmacy>) : BaseAdapter() {
    var ctx = _ctx
    val listPharmacies = _listPharmacies

    private data class ViewHolder(
        var name: TextView,
        var address: TextView,
        var checkIn: TextView,
        var checkOut: TextView
    )


    override fun getItem(p0: Int) = listPharmacies.get(p0)

    override fun getItemId(p0: Int) = listPharmacies.get(p0).hashCode().toLong()

    override fun getCount() = listPharmacies.size

    override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {

        var view = p0
        var viewHolder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.pharmacy_layout, parent, false)
//            val imageList = view?.findViewById<ImageView>(R.id.listimage) as ImageView

            val name = view?.findViewById<TextView>(R.id.name) as TextView
            val address = view?.findViewById<TextView>(R.id.address) as TextView
            val checkIn = view?.findViewById<TextView>(R.id.checkIn) as TextView
            val checkOut = view?.findViewById<TextView>(R.id.checkOut) as TextView
            viewHolder = ViewHolder(name, address, checkIn, checkOut)
            view.setTag(viewHolder)
        } else {
            viewHolder = view.getTag() as ViewHolder
        }
//        Glide.with(ctx).load(imageBaseUrl + listPharmacies.get(position).name)
//            .apply(
//                RequestOptions().placeholder(R.drawable.place_holder)
//            ).into(viewHolder.imageList)

        viewHolder.name.setText(listPharmacies.get(position).name)
        viewHolder.address.setText(listPharmacies.get(position).address)
        viewHolder.checkIn.setText(listPharmacies.get(position).check_in)
        viewHolder.checkOut.setText(listPharmacies.get(position).check_out)
        return view

    }

}